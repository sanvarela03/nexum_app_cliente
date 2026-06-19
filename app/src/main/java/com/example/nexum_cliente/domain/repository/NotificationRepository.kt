package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.notification.local.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun saveNotification(notificationEntity: NotificationEntity)
    suspend fun deleteNotification(notificationId: Int)
    fun getNotifications(): Flow<List<NotificationEntity>>
}