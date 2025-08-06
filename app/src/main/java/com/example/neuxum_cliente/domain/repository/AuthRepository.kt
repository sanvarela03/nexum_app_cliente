package com.example.neuxum_cliente.domain.repository

import com.example.neuxum_cliente.data.auth.remote.payload.req.SignInReq
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.neuxum_cliente.data.auth.remote.payload.res.SignInRes
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.data.global_payload.res.MessageRes
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signIn(signInRequest: SignInReq): Flow<ApiResponse<SignInRes>>
    fun signOut(): Flow<ApiResponse<MessageRes>>
    fun signUp(signUpReq: SignUpReq): Flow<ApiResponse<MessageRes>>
    fun authenticate(): Flow<Boolean>
}