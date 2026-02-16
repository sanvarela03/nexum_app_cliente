package com.example.nexum_cliente.ui.presenter.job_offer

import android.net.Uri

sealed class JobOfferEvent {
    data class TitleChanged(val title: String) : JobOfferEvent()
    data class DescriptionChanged(val description: String) : JobOfferEvent()
    data class AddressChanged(val address: String) : JobOfferEvent()
    data class LatitudeChanged(val latitude: Double) : JobOfferEvent()
    data class LongitudeChanged(val longitude: Double) : JobOfferEvent()
    data class AddImage(val images: List<Uri>) : JobOfferEvent()
    data class RemoveImage(val index: Int) : JobOfferEvent()
    data class DateOptionSelected(val option: String) : JobOfferEvent()
    data class TimeOptionSelected(val option: String) : JobOfferEvent()
    data class ShowDatePicker(val show: Boolean) : JobOfferEvent()
    data class ShowTimePicker(val show: Boolean) : JobOfferEvent()
    data class DateSelected(val dateMillis: Long) : JobOfferEvent()

    data class CategoryIdChanged(val categoryId: Long) : JobOfferEvent()
    data class TimeSelected(val hour: Int, val minute: Int) : JobOfferEvent()

    object DismissSuccessDialog : JobOfferEvent()
    object ConfirmSuccessDialog : JobOfferEvent()

    object Submit : JobOfferEvent()
}