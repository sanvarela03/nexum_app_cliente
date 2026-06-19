package com.example.nexum_cliente.domain.use_cases.notification

import com.example.nexum_cliente.domain.repository.NotificationRepository
import javax.inject.Inject


class DeleteNotification @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: Int) {
        repository.deleteNotification(notificationId)
    }
}