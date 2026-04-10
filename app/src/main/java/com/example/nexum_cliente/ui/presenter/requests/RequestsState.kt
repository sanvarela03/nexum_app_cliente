package com.example.nexum_cliente.ui.presenter.requests

import com.example.nexum_cliente.domain.model.JobOffer

data class RequestsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String = "",
    val jobOffers: List<JobOffer> = emptyList(),
)
