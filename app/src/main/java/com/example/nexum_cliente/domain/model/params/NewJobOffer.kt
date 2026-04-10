package com.example.nexum_cliente.domain.model.params

data class NewJobOffer(
    val title: String,
    val description: String,
    val categoryId: Long,
    val requestedDate: String,
    val photos: List<String>,
    val location: List<Double>,
)