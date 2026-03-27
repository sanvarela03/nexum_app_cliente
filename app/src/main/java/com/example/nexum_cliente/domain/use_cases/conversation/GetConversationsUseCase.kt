package com.example.nexum_cliente.domain.use_cases.conversation

import com.example.nexum_cliente.domain.model.Conversation
import com.example.nexum_cliente.domain.model.PageResponse
import com.example.nexum_cliente.domain.repository.MessagingRepository
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(
    private val repository: MessagingRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Result<PageResponse<Conversation>> {
        return repository.getConversations(page, size)
    }
}
