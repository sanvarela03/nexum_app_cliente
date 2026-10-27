package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import com.example.nexum_cliente.data.review.remote.payload.res.ReviewRes
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.model.NearbyWorker

data class JobOfferTrackingState(
    val jobOffer: JobOffer? = null,
    val workers: List<NearbyWorker> = emptyList(),
    val applications: List<JobApplication> = emptyList(),
    val errorMessage: String = "",
    val isRefreshing: Boolean = false,
    val selectedApplication: JobApplication? = null,
    val workerReviews: List<ReviewRes> = emptyList(),
    val isLoadingReviews: Boolean = false,
    val hasExistingContract: Boolean = false,
    val existingContractId: Long? = null
)
