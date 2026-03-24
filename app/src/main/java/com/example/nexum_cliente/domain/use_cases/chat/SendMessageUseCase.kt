package com.example.nexum_cliente.domain.use_cases.chat

import com.example.nexum_cliente.data.message.remote.payload.req.MessageRequest
import com.example.nexum_cliente.domain.repository.MessagingRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val msgRepository: MessagingRepository
) {
    suspend operator fun invoke(request: MessageRequest) = msgRepository.sendMessageViaWebSocket(request)
}