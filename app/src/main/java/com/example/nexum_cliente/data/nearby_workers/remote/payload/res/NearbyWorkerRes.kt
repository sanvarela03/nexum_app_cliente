package com.example.nexum_cliente.data.nearby_workers.remote.payload.res

data class NearbyWorkerRes(
    val workerId: Long,
    val userId: Long,
    val firstName: String?,
    val lastName: String?,
    val profileImageUrl: String?,
    val bio: String?,
    val distanceInMeters: Double,
    val firebaseToken: String?,
    val longitude: Double,
    val latitude: Double,
    val h3Index: String
)