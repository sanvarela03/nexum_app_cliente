package com.example.nexum_cliente.domain.use_cases.chat

import com.example.nexum_cliente.domain.repository.MessagingRepository
import javax.inject.Inject

class GetIncomingMessagesUseCase @Inject constructor(
    private val msgRepository : MessagingRepository
) {
    operator fun invoke() = msgRepository.incomingMessages
}
