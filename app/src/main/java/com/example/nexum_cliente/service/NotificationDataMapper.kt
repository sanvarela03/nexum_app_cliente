package com.example.nexum_cliente.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.nexum_cliente.data.notification.local.NotificationEntity
import com.google.firebase.messaging.RemoteMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Mapea un RemoteMessage de Firebase a un NotificationEntity de Room.
 *
 * Extrae título, cuerpo y todos los campos del data payload,
 * centralizando la lógica de parsing en un solo lugar.
 */
object NotificationDataMapper {

    private const val TAG = "NotificationDataMapper"

    /**
     * Convierte un [RemoteMessage] a un [NotificationEntity].
     *
     * @return NotificationEntity si el mensaje tiene título y cuerpo, null en caso contrario.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun toNotificationEntity(message: RemoteMessage): NotificationEntity? {
        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]

        if (title == null || body == null) {
            Log.w(TAG, "Mensaje FCM recibido sin título o cuerpo, ignorando.")
            return null
        }

        val createdAt = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"))

        val data = message.data

        return NotificationEntity(
            title = title,
            body = body,
            createdAt = createdAt,
            type = data["type"],
            jobApplicationId = data["jobApplicationId"],
            jobOfferId = data["jobOfferId"],
            jobOfferTitle = data["jobOfferTitle"],
            workerName = data["workerName"],
            workerImageUrl = data["workerImageUrl"],
            locationName = data["locationName"],
            distanceInMeters = com.example.nexum_cliente.utils.DistanceUtils.formatDistance(data["distanceInMeters"]),
            workerBio = data["workerBio"],
            laborCost = data["laborCost"]?.toDoubleOrNull(),
            materialsCost = data["materialsCost"]?.toDoubleOrNull(),
            totalCost = data["totalCost"]?.toDoubleOrNull(),
            workerAverageRating = data["workerAverageRating"]?.toDoubleOrNull(),
            workerTotalReviews = data["workerTotalReviews"]?.toIntOrNull()
        )
    }
}
