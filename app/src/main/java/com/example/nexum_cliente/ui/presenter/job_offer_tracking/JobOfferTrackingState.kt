package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import com.example.nexum_cliente.data.category.local.CategoryEntity
import com.example.nexum_cliente.domain.model.NearbyWorker

data class JobOfferTrackingState(
    val workers: List<NearbyWorker> = emptyList(),
    val errorMessage: String = "",
    val isRefreshing: Boolean = false,
)
