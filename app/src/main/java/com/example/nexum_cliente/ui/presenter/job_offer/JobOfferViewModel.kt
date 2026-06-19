package com.example.nexum_cliente.ui.presenter.job_offer

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.common.JOB_OFFER_URI_URL_CACHE_KEY
import com.example.nexum_cliente.common.MAX_JOB_OFFER_IMAGES
import com.example.nexum_cliente.common.MediaUploadHandler
import com.example.nexum_cliente.data.job_offer.mapper.JobOfferMapper
import com.example.nexum_cliente.domain.use_cases.common.MediaManagementUseCase
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import com.example.nexum_cliente.utils.DateUtils
import com.example.nexum_cliente.utils.validator.JobOfferValidator
import com.example.nexum_cliente.utils.location.LocationClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class JobOfferViewModel @Inject constructor(
    private val jobOfferUseCases: JobOfferUseCases,
    private val locationClient: LocationClient,
    private val mediaUploadHandler: MediaUploadHandler,
    private val mediaManagementUseCase: MediaManagementUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "JobOfferViewModel"
    }

    private val _state = MutableStateFlow(JobOfferState())
    val state = _state.asStateFlow()

    var showMapDialog by mutableStateOf(false)

    /**
     * Scope independiente del viewModelScope para ejecutar limpieza en onCleared().
     * viewModelScope ya está cancelado cuando onCleared() se ejecuta, por lo que
     * necesitamos un scope propio que podamos controlar manualmente.
     */
    private val cleanupJob = SupervisorJob()
    private val cleanupScope = CoroutineScope(cleanupJob + Dispatchers.IO)

    /** Flag que indica si la oferta fue publicada exitosamente.
     *  Si es true en onCleared(), NO se borran las imágenes de Firebase. */
    private var wasSubmittedSuccessfully = false

    /**
     * Cache en memoria: URI local → URL de Firebase.
     * Evita subir la misma imagen dos veces si el usuario la selecciona de nuevo.
     */
    private val uploadedUriCache = mutableMapOf<android.net.Uri, String>()

    /**
     * URLs que provienen del cache persistido en DataStore (ofertas ya publicadas).
     * Estas NO deben borrarse de Firebase si el usuario cancela,
     * ya que pertenecen a ofertas anteriores.
     */
    private val persistedCachedUrls = mutableSetOf<String>()

    private val dateFormatter = SimpleDateFormat(DateUtils.DATE_PATTERN, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(DateUtils.TIME_PATTERN, Locale.ENGLISH)

    init {
        loadUriUrlCache()
    }

    /**
     * Carga el cache URI→URL persistido desde DataStore al iniciar.
     * Así los lookups son sincónicos durante la sesión.
     */
    private fun loadUriUrlCache() {
        viewModelScope.launch {
            val persistedCache = mediaManagementUseCase.getUriUrlCache(JOB_OFFER_URI_URL_CACHE_KEY)
            persistedCache.forEach { (uriStr, url) ->
                uploadedUriCache[android.net.Uri.parse(uriStr)] = url
                persistedCachedUrls.add(url) // Marcar como "no borrar en cancel"
            }
            Log.d(TAG, "💾 Cache cargado: ${persistedCache.size} entradas")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: JobOfferEvent) {
        when (event) {
            is JobOfferEvent.AddressChanged -> _state.update { it.copy(address = event.address) }
            is JobOfferEvent.DescriptionChanged -> _state.update {
                it.copy(description = event.description)
            }

            is JobOfferEvent.TitleChanged -> _state.update { it.copy(title = event.title) }
            is JobOfferEvent.AddImage -> _state.update { it.copy(images = it.images + event.images) }
            is JobOfferEvent.RemoveImage -> _state.update {
                it.copy(images = it.images.toMutableList().apply { removeAt(event.index) })
            }

            // --- Eventos de subida real a Firebase ---
            is JobOfferEvent.ImageUriAdded -> {
                // Bloquear si ya se alcanzó el límite
                if (_state.value.imageUrls.size >= MAX_JOB_OFFER_IMAGES) return

                // Verificar si esta URI ya fue subida antes (evitar duplicados en Firebase)
                val cachedUrl = uploadedUriCache[event.uri]
                if (cachedUrl != null) {
                    Log.d(TAG, "📦 URI ya subida, reutilizando URL del cache: $cachedUrl")
                    _state.update { it.copy(images = it.images + event.uri) }
                    onEvent(JobOfferEvent.ImageUrlUploaded(cachedUrl))
                    return
                }

                // 1. Añadir la Uri local para preview inmediato e incrementar contador
                _state.update { it.copy(images = it.images + event.uri, uploadingCount = it.uploadingCount + 1) }

                // 2. Subir a Firebase Storage bajo "job_offer_images/"
                mediaUploadHandler.handleImageUpload(
                    scope = viewModelScope,
                    uri = event.uri,
                    folder = "job_offer_images",
                    onSuccess = { url ->
                        uploadedUriCache[event.uri] = url // Cache en memoria
                        onEvent(JobOfferEvent.ImageUrlUploaded(url))
                    },
                    onError = {
                        // Revertir el preview local y decrementar contador si falla
                        _state.update {
                            it.copy(
                                images = it.images.dropLast(1),
                                uploadingCount = (it.uploadingCount - 1).coerceAtLeast(0),
                                errorMessage = "Error al subir imagen"
                            )
                        }
                    }
                )
            }

            is JobOfferEvent.ImageUrlUploaded -> {
                _state.update {
                    it.copy(
                        imageUrls = it.imageUrls + event.url,
                        uploadingCount = (it.uploadingCount - 1).coerceAtLeast(0)
                    )
                }
            }

            is JobOfferEvent.ImageUrlRemoved -> {
                // Borrar de Firebase y del estado (solo memoria)
                mediaUploadHandler.handleImageDelete(
                    scope = viewModelScope,
                    url = event.url,
                    onSuccess = {
                        uploadedUriCache.entries.removeIf { it.value == event.url }
                        _state.update { it.copy(imageUrls = it.imageUrls - event.url) }
                    },
                    onError = {
                        _state.update { it.copy(errorMessage = "Error al eliminar imagen") }
                    }
                )
            }

            is JobOfferEvent.LatitudeChanged -> _state.update { it.copy(latitude = event.latitude) }
            is JobOfferEvent.LongitudeChanged -> _state.update { it.copy(longitude = event.longitude) }
            is JobOfferEvent.DateOptionSelected -> {
                _state.update { it.copy(selectedDateOption = event.option) }
                when (event.option) {
                    "Hoy" -> {
                        val calendar = Calendar.getInstance()
                        updateDate(calendar.timeInMillis)
                    }

                    "3 dias" -> {
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR, 3)
                        updateDate(calendar.timeInMillis)
                    }

                    "1 semana" -> {
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.WEEK_OF_YEAR, 1)
                        updateDate(calendar.timeInMillis)
                    }

                    "Elegir" -> {
                        _state.update { it.copy(showDatePickerDialog = true) }
                    }
                }
            }

            is JobOfferEvent.TimeOptionSelected -> {
                _state.update { it.copy(selectedTimeOption = event.option) }
                when (event.option) {
                    "Mañana" -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, 10)
                        calendar.set(Calendar.MINUTE, 0)
                        _state.update { it.copy(requestedTime = timeFormatter.format(calendar.time)) }
                    }

                    "Tarde" -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, 18)
                        calendar.set(Calendar.MINUTE, 0)
                        _state.update { it.copy(requestedTime = timeFormatter.format(calendar.time)) }
                    }

                    "Noche" -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, 22) // 10 PM
                        calendar.set(Calendar.MINUTE, 0)
                        _state.update { it.copy(requestedTime = timeFormatter.format(calendar.time)) }
                    }

                    "Elegir" -> {
                        _state.update { it.copy(showTimePickerDialog = true) }
                    }
                }
            }

            is JobOfferEvent.ShowDatePicker -> _state.update { it.copy(showDatePickerDialog = event.show) }
            is JobOfferEvent.ShowTimePicker -> _state.update { it.copy(showTimePickerDialog = event.show) }
            is JobOfferEvent.DateSelected -> {
                updateDate(event.dateMillis)
            }

            is JobOfferEvent.TimeSelected -> {
                val validationResult = JobOfferValidator.validateTime(event.hour, event.minute, _state.value.requestedDate)
                if (validationResult.isValid) {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, event.hour)
                        set(Calendar.MINUTE, event.minute)
                    }
                    _state.update {
                        it.copy(
                            requestedTime = timeFormatter.format(calendar.time),
                            errorMessage = ""
                        )
                    }
                } else {
                    _state.update { it.copy(errorMessage = validationResult.errorMessage) }
                }
            }

            is JobOfferEvent.CategoryIdChanged -> _state.update { it.copy(categoryId = event.categoryId) }

            JobOfferEvent.RequestCurrentLocation -> {
                locationClient.getCurrentLocation(object : LocationClient.LocationCallback {
                    override fun onLocationResult(latLng: LatLng, address: String) {
                        _state.update {
                            it.copy(
                                latitude = latLng.latitude,
                                longitude = latLng.longitude,
                                address = address
                            )
                        }
                    }

                    override fun onError(message: String) {
                        _state.update { it.copy(errorMessage = message) }
                    }
                })
            }

            JobOfferEvent.Submit -> {
                submitJobOffer()
            }

            JobOfferEvent.ConfirmSuccessDialog -> {
                _state.update { it.copy(isJobOfferSubmitted = false) }
            }

            JobOfferEvent.DismissSuccessDialog -> {
                _state.update { it.copy(isJobOfferSubmitted = false) }
            }
        }
    }

    fun getInitialDateMillis(): Long? {
        return try {
            dateFormatter.parse(_state.value.requestedDate)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun getInitialTimeValues(): IntArray {
        return try {
            val date = timeFormatter.parse(_state.value.requestedTime)
            if (date != null) {
                val cal = Calendar.getInstance().apply { time = date }
                intArrayOf(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
            } else {
                val now = Calendar.getInstance()
                intArrayOf(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
            }
        } catch (e: Exception) {
            val now = Calendar.getInstance()
            intArrayOf(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
        }
    }

    private fun updateDate(dateMillis: Long) {
        val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = dateMillis
        dateFormatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
        _state.update { it.copy(requestedDate = dateFormatter.format(calendar.time)) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitJobOffer() {
        viewModelScope.launch {
            Log.d("JobOfferViewModel", "Requested date: ${_state.value.requestedDate}")
            Log.d("JobOfferViewModel", "Requested time: ${_state.value.requestedTime}")
            val jobOffer = JobOfferMapper.stateToDomain(_state.value)
            Log.d("JobOfferViewModel", "Submitting job offer: $jobOffer")
            jobOfferUseCases.createJobOffer(jobOffer).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val msg =
                            "Oferta de trabajo ${response.data.jobOfferUuid} creada con éxito."
                        wasSubmittedSuccessfully = true // 🔒 No limpiar imágenes al salir
                        // Guardar en DataStore SOLO las URIs usadas en este submit exitoso
                        val uriToUrlMap = uploadedUriCache
                            .filterValues { url -> _state.value.imageUrls.contains(url) }
                            .map { (uri, url) -> uri.toString() to url }
                            .toMap()
                        viewModelScope.launch {
                            mediaManagementUseCase.saveUriUrlsToCache(
                                JOB_OFFER_URI_URL_CACHE_KEY, uriToUrlMap
                            )
                        }
                        _state.update {
                            it.copy(
                                isJobOfferSubmitted = true,
                                isLoading = false,
                                createdJobOfferUuid = response.data.jobOfferUuid,
                                successMessage = msg
                            )
                        }
                    }

                    is ApiResponse.Error -> {
                        _state.update {
                            it.copy(
                                isJobOfferSubmitted = false,
                                errorMessage = response.errorMessage,
                                isLoading = false
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _state.update {
                            it.copy(
                                isJobOfferSubmitted = false,
                                errorMessage = response.errorMessage,
                                isLoading = false
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _state.update { it.copy(isJobOfferSubmitted = false, isLoading = true) }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!wasSubmittedSuccessfully) {
            // Solo borrar URLs que se subieron en ESTA sesión y no fueron publicadas.
            // Las URLs del cache persistente (ofertas anteriores) NO se tocan.
            val urlsToDelete = _state.value.imageUrls
                .filter { url -> !persistedCachedUrls.contains(url) }

            if (urlsToDelete.isNotEmpty()) {
                Log.d(TAG, "🧹 Limpiando ${urlsToDelete.size} imágenes huérfanas de Firebase (${persistedCachedUrls.size} protegidas del cache)...")
                cleanupScope.launch {
                    urlsToDelete.forEach { url ->
                        runCatching { mediaManagementUseCase.deleteImageOnly(url) }
                            .onSuccess { Log.d(TAG, "✅ Imagen eliminada: $url") }
                            .onFailure { Log.e(TAG, "❌ Error al eliminar imagen: $url", it) }
                    }
                }.invokeOnCompletion {
                    cleanupJob.cancel()
                }
            } else {
                Log.d(TAG, "💾 No hay imágenes nuevas que limpiar (todas son del cache).")
                cleanupJob.cancel()
            }
        } else {
            Log.d(TAG, "✅ Oferta publicada exitosamente. No se limpian imágenes.")
            cleanupJob.cancel()
        }
    }
}