package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun updateFcmToken(fcmToken: String): Flow<ApiResponse<Unit>>
}
