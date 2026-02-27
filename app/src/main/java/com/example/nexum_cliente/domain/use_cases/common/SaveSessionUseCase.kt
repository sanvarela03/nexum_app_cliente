package com.example.nexum_cliente.domain.use_cases.common

import android.util.Log
import com.example.nexum_cliente.data.auth.remote.payload.res.SignInRes
import com.example.nexum_cliente.security.TokenManager
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(response: SignInRes) {
        Log.d("SaveSessionUseCase", "response: $response")
        tokenManager.saveAccessToken(response.token)
        tokenManager.saveRefreshToken(response.refreshToken)
        Log.d("SaveSessionUseCase", "Saving user ID: ${response.id}")
        tokenManager.saveUserId(response.id)
    }
}
