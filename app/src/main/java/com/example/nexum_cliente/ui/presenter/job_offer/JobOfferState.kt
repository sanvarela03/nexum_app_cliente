package com.example.nexum_cliente.ui.presenter.job_offer

import android.net.Uri
import com.example.nexum_cliente.common.MAX_JOB_OFFER_IMAGES
import com.example.nexum_cliente.ui.common.ValidationResult
import com.example.nexum_cliente.utils.validator.JobOfferValidator

data class JobOfferState(
    val categoryId: Long = -1L,
    val title: String? = "",
    val description: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val images: List<Uri> = emptyList(),          // URIs locales para preview
    val imageUrls: List<String> = emptyList(),    // URLs remotas subidas a Firebase
    val uploadingCount: Int = 0,                  // Contador de uploads en progreso
    val requestedDate: String = "",
    val requestedTime: String = "",
    val selectedDateOption: String = "",
    val selectedTimeOption: String = "",
    val showDatePickerDialog: Boolean = false,
    val showTimePickerDialog: Boolean = false,
    val isLoading: Boolean = false,

    val isJobOfferSubmitted: Boolean = false,
    val createdJobOfferUuid: String = "",
    val errorMessage: String = "",
    val successMessage: String = ""
) {
    val titleValidation: ValidationResult
        get() = JobOfferValidator.validateTitle(title)

    val descriptionValidation: ValidationResult
        get() = JobOfferValidator.validateDescription(description)

    val addressValidation: ValidationResult
        get() = JobOfferValidator.validateAddress(address)

    val imagesValidation: ValidationResult
        get() = JobOfferValidator.validateImages(imageUrls)

    val dateValidation: ValidationResult
        get() = JobOfferValidator.validateDate(requestedDate)

    val timeValidation: ValidationResult
        get() = JobOfferValidator.validateTime(requestedDate, requestedTime)

    val isFormValid: Boolean
        get() = titleValidation.isValid &&
                descriptionValidation.isValid &&
                addressValidation.isValid &&
                imagesValidation.isValid &&
                dateValidation.isValid &&
                timeValidation.isValid &&
                uploadingCount == 0 &&   // Todas las fotos terminaron de subirse
                latitude != 0.0 &&
                longitude != 0.0

    /** true si hay alguna imagen subiendo en este momento */
    val isUploadingImage: Boolean
        get() = uploadingCount > 0

    /** true cuando ya se alcanzó el límite: la UI debe ocultar el botón de añadir foto */
    val isAtImageLimit: Boolean
        get() = imageUrls.size >= MAX_JOB_OFFER_IMAGES

    /** Muestra cuántos slots quedan, útil para un contador "3/5" en la UI */
    val remainingImageSlots: Int
        get() = (MAX_JOB_OFFER_IMAGES - imageUrls.size).coerceAtLeast(0)
}
