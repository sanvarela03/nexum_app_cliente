package com.example.nexum_cliente.data.nearby_workers.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["workerId", "jobOfferId"])
data class NearbyWorkerEntity(
    val workerId: Long,
    val jobOfferId: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String,
    val bio: String,
    val distanceInMeters: Double,
    val firebaseToken: String,
    val longitude: Double,
    val latitude: Double,
    val h3Index: String
)
