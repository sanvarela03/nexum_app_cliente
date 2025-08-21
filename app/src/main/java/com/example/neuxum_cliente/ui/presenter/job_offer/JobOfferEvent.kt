package com.example.neuxum_cliente.ui.presenter.job_offer

import android.net.Uri


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/12/2025
 * @version 1.0
 */
sealed class JobOfferEvent {
    data class TitleChanged(val title: String) : JobOfferEvent()
    data class DescriptionChanged(val description: String) : JobOfferEvent()
    data class AddressChanged(val address: String) : JobOfferEvent()
    data class CategoryIdChanged(val categoryId: Long) : JobOfferEvent()
    data class RequestedDateChanged(val requestedDate: String) : JobOfferEvent()
    data class LatitudeChanged(val latitude: Double) : JobOfferEvent()
    data class LongitudeChanged(val longitude: Double) : JobOfferEvent()
    data class AddImage(val newImage: Uri) : JobOfferEvent()
    data class RemoveImage(val index: Int) : JobOfferEvent()
    object Submit : JobOfferEvent()
}