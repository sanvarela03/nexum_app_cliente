package com.example.nexum_trabajador.domain.use_cases.sign_in

import com.example.nexum_trabajador.security.TokenManager
import javax.inject.Inject

class SaveLastUserEmailUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(email: String) {
        tokenManager.saveLastUserEmail(email)
    }
}
