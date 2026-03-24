package com.example.nexum_cliente.domain.use_cases.conversation

import com.example.nexum_cliente.domain.model.Message
import com.example.nexum_cliente.domain.repository.MessagingRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ObserveIncomingMessagesUseCase @Inject constructor(
    private val repository: MessagingRepository
) {
    operator fun invoke(): SharedFlow<Message?> {
        return repository.incomingMessages
    }
}
