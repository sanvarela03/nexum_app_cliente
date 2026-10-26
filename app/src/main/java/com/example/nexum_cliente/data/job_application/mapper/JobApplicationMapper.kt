package com.example.nexum_cliente.data.job_application.mapper

import com.example.nexum_cliente.data.job_application.remote.payload.res.JobApplicationRes
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.domain.model.MaterialItem
import com.example.nexum_cliente.utils.DistanceUtils

fun JobApplicationRes.toDomain(jobOfferId: String = ""): JobApplication {
    val labor = laborCost ?: 0.0
    val domainMaterials = materials?.map {
        val qty = it.quantity ?: 1
        val price = it.unitPrice ?: 0.0
        MaterialItem(
            id = it.id ?: 0L,
            name = it.name ?: "Material",
            description = it.description ?: "",
            type = it.type ?: "",
            quantity = qty,
            unitPrice = price,
            subtotal = qty * price
        )
    } ?: emptyList()

    val materialsSum = if (materialsCost != null && materialsCost > 0) {
        materialsCost
    } else if (domainMaterials.isNotEmpty()) {
        domainMaterials.sumOf { it.subtotal }
    } else {
        0.0
    }

    val total = totalCost ?: (labor + materialsSum)

    return JobApplication(
        jobApplicationId = (jobApplicationId ?: 0L).toString(),
        jobOfferId = jobOfferId,
        jobOfferTitle = "",
        workerId = workerId,
        workerUserId = workerUserId,
        workerName = workerName ?: "Trabajador",
        workerImageUrl = workerImageUrl ?: "",
        locationName = "",
        distanceInMeters = distanceInMeters?.let { DistanceUtils.formatDistance(it) } ?: "",
        workerBio = workerBio ?: "",
        proposalText = proposalText ?: "",
        scheduledDateTime = scheduledDateTime ?: "",
        laborCost = labor,
        materialsCost = materialsSum,
        totalCost = total,
        workerAverageRating = workerAverageRating ?: 0.0,
        workerTotalReviews = workerTotalReviews ?: 0,
        materials = domainMaterials
    )
}

fun List<JobApplicationRes>.toDomain(jobOfferId: String = ""): List<JobApplication> =
    map { it.toDomain(jobOfferId) }
