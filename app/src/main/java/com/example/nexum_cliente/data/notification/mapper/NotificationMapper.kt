package com.example.nexum_cliente.data.notification.mapper

import com.example.nexum_cliente.data.notification.local.NotificationEntity
import com.example.nexum_cliente.domain.model.JobApplication

object NotificationMapper {
    fun toJobApplication(notification: NotificationEntity): JobApplication {
        return JobApplication(
            jobApplicationId = notification.jobApplicationId ?: "",
            jobOfferId = notification.jobOfferId ?: "",
            jobOfferTitle = notification.jobOfferTitle ?: "",
            workerName = notification.workerName ?: "",
            workerImageUrl = notification.workerImageUrl ?: "",
            locationName = notification.locationName ?: "",
            distanceInMeters = com.example.nexum_cliente.utils.DistanceUtils.formatDistance(notification.distanceInMeters),
            workerBio = notification.workerBio ?: "",
            laborCost = notification.laborCost ?: 0.0,
            materialsCost = notification.materialsCost ?: 0.0,
            totalCost = notification.totalCost ?: 0.0,
            workerAverageRating = notification.workerAverageRating ?: 0.0,
            workerTotalReviews = notification.workerTotalReviews ?: 0
        )
    }

    fun toJobApplication(notifications: List<NotificationEntity>): List<JobApplication> =
        notifications.map { toJobApplication(it) }

}