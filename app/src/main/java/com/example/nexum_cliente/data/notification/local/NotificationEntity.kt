package com.example.nexum_cliente.data.notification.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val body: String,
    val createdAt: String,

    val type: String? = null,
    val jobApplicationId: String? = null,
    val jobOfferId: String? = null,
    val jobOfferTitle: String? = null,
    val workerName: String? = null,
    val workerImageUrl: String? = null,
    val locationName: String? = null,
    val distanceInMeters: String? = null,
    val workerBio: String? = null,
    val laborCost: Double? = null,
    val materialsCost: Double? = null,
    val totalCost: Double? = null,
    val workerAverageRating: Double? = null,
    val workerTotalReviews: Int? = null
)