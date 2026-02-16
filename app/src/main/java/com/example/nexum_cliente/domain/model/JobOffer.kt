package com.example.nexum_cliente.domain.model

<<<<<<< Updated upstream
data class JobOffer()
=======
data class JobOffer(
    val title: String,
    val description: String,
    val categoryId: Long,
    val requestedDate: String,
    val photos: List<String>,
    val location: List<Double>,
)
>>>>>>> Stashed changes
