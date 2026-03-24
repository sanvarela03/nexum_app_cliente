package com.example.nexum_cliente.domain.use_cases.chat

import com.example.nexum_cliente.domain.model.Message
import com.example.nexum_cliente.domain.model.PageResponse
import com.example.nexum_cliente.domain.repository.MessagingRepository
import javax.inject.Inject

class LoadConversationMessagesUseCase @Inject constructor (
    private val msgRepository: MessagingRepository
) {
    suspend operator fun invoke(conversationId: String, currentPage: Int, pageSize: Int): Result<PageResponse<Message>> {
        return msgRepository.getConversationMessages(conversationId, currentPage, pageSize);
    }
}