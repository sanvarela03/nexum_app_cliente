package com.example.neuxum_cliente.ui.presenter.job_offer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@HiltViewModel
class JobOfferViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(JobOfferState())

    var showMapDialog by mutableStateOf(false)

    fun onEvent(event: JobOfferEvent) {
        when (event) {
            is JobOfferEvent.AddressChanged -> {
                state = state.copy(address = event.address)
            }

            is JobOfferEvent.CategoryIdChanged -> {
                state = state.copy(categoryId = event.categoryId)
            }

            is JobOfferEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            is JobOfferEvent.AddImage -> {
                Log.d("JobOfferViewModel", "Event: ${event.newImage}")
                Log.d("JobOfferViewModel", "State before: ${state.images}")
                state.images.add(event.newImage)
//                state = state.copy(images = state.images.add(event.newImage))
                Log.d("JobOfferViewModel", "State after: ${state.images}")
            }

            is JobOfferEvent.LatitudeChanged -> {
                state = state.copy(latitude = event.latitude)
            }

            is JobOfferEvent.LongitudeChanged -> {
                state = state.copy(longitude = event.longitude)
            }

            is JobOfferEvent.RequestedDateChanged -> {
                state = state.copy(requestedDate = event.requestedDate)
            }

            is JobOfferEvent.TitleChanged -> {
                state = state.copy(title = event.title)
            }

            is JobOfferEvent.RemoveImage -> {
                state.images.removeAt(event.index)
            }

            JobOfferEvent.Submit -> {

            }
        }
    }
}