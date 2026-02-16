<<<<<<< Updated upstream
package com.example.nexum_trabajador.domain.use_cases.common

import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.security.TokenManager
=======
package com.example.nexum_cliente.domain.use_cases.common

import android.util.Log
import com.example.nexum_cliente.data.auth.remote.payload.res.SignInRes
import com.example.nexum_cliente.security.TokenManager
>>>>>>> Stashed changes
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(response: SignInRes) {
<<<<<<< Updated upstream
        tokenManager.saveAccessToken(response.token)
        tokenManager.saveRefreshToken(response.refreshToken)
        tokenManager.saveUserId(response.userId)
=======
        Log.d("SaveSessionUseCase", "response: $response")
        tokenManager.saveAccessToken(response.token)
        tokenManager.saveRefreshToken(response.refreshToken)
        Log.d("SaveSessionUseCase", "Saving user ID: ${response.id}")
        tokenManager.saveUserId(response.id)
>>>>>>> Stashed changes
    }
}
