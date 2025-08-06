package com.example.neuxum_cliente.domain.use_cases.auth

import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.data.global_payload.res.MessageRes
import com.example.neuxum_cliente.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignOut(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<ApiResponse<MessageRes>> {
        return repository.signOut()
    }
}