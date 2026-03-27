package com.example.nexum_cliente.domain.use_cases.chat

import com.example.nexum_cliente.domain.repository.MessagingRepository
import javax.inject.Inject

class DisconnectWebSocketUseCase @Inject constructor(
    private val msgRepository : MessagingRepository
) {
    suspend operator fun invoke() = msgRepository.disconnectWebSocket()
}
