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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class JobOfferViewModel @Inject constructor(
    private val jobOfferUseCases: JobOfferUseCases
) : ViewModel() {

    var state by mutableStateOf(JobOfferState())
        private set

    var showMapDialog by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: JobOfferEvent) {
        when (event) {
            is JobOfferEvent.AddressChanged -> state = state.copy(address = event.address)
            is JobOfferEvent.DescriptionChanged -> state =
                state.copy(description = event.description)

            is JobOfferEvent.TitleChanged -> state = state.copy(title = event.title)
            is JobOfferEvent.AddImage -> state = state.copy(images = state.images + event.images)
            is JobOfferEvent.RemoveImage -> state =
                state.copy(images = state.images.toMutableList().apply { removeAt(event.index) })

            is JobOfferEvent.LatitudeChanged -> state = state.copy(latitude = event.latitude)
            is JobOfferEvent.LongitudeChanged -> state = state.copy(longitude = event.longitude)
            is JobOfferEvent.DateOptionSelected -> {
                state = state.copy(selectedDateOption = event.option)
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
                        state = state.copy(showDatePickerDialog = true)
                    }
                }
            }

            is JobOfferEvent.TimeOptionSelected -> {
                state = state.copy(selectedTimeOption = event.option)
                when (event.option) {
                    "Mañana" -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, 10)
                        calendar.set(Calendar.MINUTE, 0)
                        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        state = state.copy(requestedTime = timeFormatter.format(calendar.time))
                    }

                    "Tarde" -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, 18)
                        calendar.set(Calendar.MINUTE, 0)
                        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        state = state.copy(requestedTime = timeFormatter.format(calendar.time))
                    }

                    "Noche" -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, 22) // 10 PM
                        calendar.set(Calendar.MINUTE, 0)
                        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        state = state.copy(requestedTime = timeFormatter.format(calendar.time))
                    }
                    
                    "Elegir" -> {
                        state = state.copy(showTimePickerDialog = true)
                    }
                }
            }

            is JobOfferEvent.ShowDatePicker -> state = state.copy(showDatePickerDialog = event.show)
            is JobOfferEvent.ShowTimePicker -> state = state.copy(showTimePickerDialog = event.show)
            is JobOfferEvent.DateSelected -> {
                updateDate(event.dateMillis)
            }

            is JobOfferEvent.TimeSelected -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, event.hour)
                    set(Calendar.MINUTE, event.minute)
                }
                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                state = state.copy(requestedTime = timeFormatter.format(calendar.time))
            }

            is JobOfferEvent.CategoryIdChanged -> state = state.copy(categoryId = event.categoryId)

            JobOfferEvent.Submit -> {
                submitJobOffer()
            }

            JobOfferEvent.ConfirmSuccessDialog -> {
                state = state.copy(isJobOfferSubmitted = false)
            }

            JobOfferEvent.DismissSuccessDialog -> {
                state = state.copy(isJobOfferSubmitted = false)
            }
        }
    }

    private fun updateDate(dateMillis: Long) {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        state = state.copy(requestedDate = dateFormatter.format(Date(dateMillis)))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitJobOffer() {
        viewModelScope.launch {
            Log.d("JobOfferViewModel", "Requested date: ${state.requestedDate}")
            Log.d("JobOfferViewModel", "Requested time: ${state.requestedTime}")
            val jobOffer = JobOfferMapper.stateToDomain(state)
            Log.d("JobOfferViewModel", "Submitting job offer: $jobOffer")
            jobOfferUseCases.createJobOffer(jobOffer).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val msg =
                            "Oferta de trabajo ${response.data.jobOfferUuid}  creada con éxito."
                        state = state.copy(
                            isJobOfferSubmitted = true,
                            isLoading = false,
                            successMessage = msg
                        )
                    }

                    is ApiResponse.Error -> {
                        state = state.copy(
                            isJobOfferSubmitted = false,
                            errorMessage = response.errorMessage,
                            isLoading = false
                        )
                    }

                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isJobOfferSubmitted = false,
                            errorMessage = response.errorMessage,
                            isLoading = false
                        )
                    }

                    is ApiResponse.Loading -> {
                        state = state.copy(isJobOfferSubmitted = false, isLoading = true)
                    }
                }
            }
        }
    }
}
