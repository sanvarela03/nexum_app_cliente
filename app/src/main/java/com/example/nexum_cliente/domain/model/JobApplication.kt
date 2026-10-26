package com.example.nexum_cliente.domain.model

data class MaterialItem(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val type: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0,
    val subtotal: Double = quantity * unitPrice
)

data class JobApplication(
    val jobApplicationId: String,
    val jobOfferId: String,
    val jobOfferTitle: String = "",
    val workerId: Long? = null,
    val workerUserId: Long? = null,
    val workerName: String,
    val workerImageUrl: String = "",
    val locationName: String = "",
    val distanceInMeters: String = "",
    val workerBio: String = "",
    val proposalText: String = "",
    val scheduledDateTime: String = "",
    val laborCost: Double = 0.0,
    val materialsCost: Double = 0.0,
    val totalCost: Double = 0.0,
    val workerAverageRating: Double = 0.0,
    val workerTotalReviews: Int = 0,
    val materials: List<MaterialItem> = emptyList()
)