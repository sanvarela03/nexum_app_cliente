package com.example.nexum_cliente.ui.presenter.job_offer

import android.net.Uri

data class JobOfferState(
    val categoryId: Long = -1L,
    val title: String = "",
    val description: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val images: List<Uri> = emptyList(),
    val requestedDate: String = "",
    val requestedTime: String = "",
    val selectedDateOption: String = "",
    val selectedTimeOption: String = "",
    val showDatePickerDialog: Boolean = false,
    val showTimePickerDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,

    val isJobOfferSubmitted: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)