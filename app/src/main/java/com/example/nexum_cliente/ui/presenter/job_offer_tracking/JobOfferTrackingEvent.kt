package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import com.example.nexum_cliente.domain.model.JobApplication

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 5/5/2026
 * @version 1.0
 */
sealed class JobOfferTrackingEvent {
    object Refresh : JobOfferTrackingEvent()
    object ClearError : JobOfferTrackingEvent()
    data class SelectApplication(val application: JobApplication?) : JobOfferTrackingEvent()
    object DismissApplicationDetail : JobOfferTrackingEvent()
}