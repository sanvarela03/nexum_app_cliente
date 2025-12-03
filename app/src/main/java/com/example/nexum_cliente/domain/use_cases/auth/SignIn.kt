package com.example.nexum_cliente.domain.use_cases.auth


import com.example.nexum_cliente.data.auth.remote.payload.req.SignInReq
import com.example.nexum_cliente.data.auth.remote.payload.res.SignInRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignIn(
    private val repository: AuthRepository
) {
    operator fun invoke(username: String, password: String, firebaseToken: String): Flow<ApiResponse<SignInRes>> {
        return repository.signIn(SignInReq(username, password, firebaseToken))
    }
}