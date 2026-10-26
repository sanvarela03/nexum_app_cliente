package com.example.nexum_cliente.ui.presenter.requests

import com.example.nexum_cliente.domain.model.JobOffer

enum class RequestsSortType {
    DATE_DESC, // Más reciente
    DATE_ASC,  // Más antigua
    STATUS     // Por estado
}

data class RequestsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String = "",
    val rawJobOffers: List<JobOffer> = emptyList(),
    val jobOffers: List<JobOffer> = emptyList(),
    val sortType: RequestsSortType = RequestsSortType.DATE_DESC,
)
