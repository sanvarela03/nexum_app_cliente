package com.example.nexum_cliente.data.notification

import com.example.nexum_cliente.data.notification.local.NotificationEntity
import com.example.nexum_cliente.data.notification.local.NotificationLocalDataSource
import com.example.nexum_cliente.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val localDataSource: NotificationLocalDataSource
) : NotificationRepository {
    override suspend fun saveNotification(notificationEntity: NotificationEntity) {
        localDataSource.insert(notificationEntity)
    }

    override suspend fun deleteNotification(notificationId: Int) {
        localDataSource.delete(notificationId)
    }

    override fun getNotifications(): Flow<List<NotificationEntity>> {
        return localDataSource.getAll()
    }
}