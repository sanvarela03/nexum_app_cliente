package com.example.nexum_cliente.domain.use_cases.auth

import com.example.nexum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.global_payload.res.MessageRes
import com.example.nexum_cliente.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignUp(
    private val repository: AuthRepository
) {
    operator fun invoke(signUpRequest: SignUpReq): Flow<ApiResponse<MessageRes>> {
        return repository.signUp(signUpRequest)
    }
}