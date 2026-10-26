package com.example.nexum_cliente.domain.use_cases.user

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(fcmToken: String): Flow<ApiResponse<Unit>> {
        return userRepository.updateFcmToken(fcmToken)
    }
}
