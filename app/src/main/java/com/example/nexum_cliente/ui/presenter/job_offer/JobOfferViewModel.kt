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
import com.example.nexum_cliente.data.job_offer.mapper.JobOfferMapper
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import com.example.nexum_cliente.utils.DateUtils
import com.example.nexum_cliente.utils.validator.JobOfferValidator
import com.example.nexum_cliente.utils.location.LocationClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val locationClient: LocationClient
) : ViewModel() {

    private val _state = MutableStateFlow(JobOfferState())
    val state = _state.asStateFlow()

    var showMapDialog by mutableStateOf(false)

    private val dateFormatter = SimpleDateFormat(DateUtils.DATE_PATTERN, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(DateUtils.TIME_PATTERN, Locale.ENGLISH)

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
        _state.update { it.copy(requestedDate = dateFormatter.format(Date(dateMillis))) }
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
}