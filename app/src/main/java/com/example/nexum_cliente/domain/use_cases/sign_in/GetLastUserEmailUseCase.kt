package com.example.nexum_trabajador.domain.use_cases.sign_in

import com.example.nexum_trabajador.security.TokenManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLastUserEmailUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(): String? {
        return tokenManager.getLastUserEmail().first()
    }
}
