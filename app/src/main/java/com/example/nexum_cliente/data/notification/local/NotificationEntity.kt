package com.example.nexum_cliente.data.notification.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NotificationEntity(
    @PrimaryKey
    val id: Int?,
    val title: String,
    val body: String,
    val createdAt: String
)