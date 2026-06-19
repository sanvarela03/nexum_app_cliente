package com.example.nexum_cliente.data.nearby_workers.mapper

import com.example.nexum_cliente.data.mapper.DomainMapper
import com.example.nexum_cliente.data.mapper.EntityMapper
import com.example.nexum_cliente.data.nearby_workers.local.NearbyWorkerEntity
import com.example.nexum_cliente.data.nearby_workers.remote.payload.res.NearbyWorkerRes
import com.example.nexum_cliente.domain.model.NearbyWorker

object NearbyWorkerMapper :
    EntityMapper<NearbyWorkerRes, NearbyWorkerEntity>,
    DomainMapper<NearbyWorkerEntity, NearbyWorker> {
    override fun toEntity(
        dto: NearbyWorkerRes,
    ): NearbyWorkerEntity {
        // Fallback or generic usage if jobOfferId isn't available, but we prefer toEntityWithOffer
        return toEntityWithOffer(dto, 0L)
    }

    fun toEntityWithOffer(
        dto: NearbyWorkerRes,
        jobOfferId: Long
    ): NearbyWorkerEntity {
        return NearbyWorkerEntity(
            workerId = dto.workerId,
            jobOfferId = jobOfferId,
            userId = dto.userId,
            firstName = dto.firstName ?: "Usuario",
            lastName = dto.lastName ?: "",
            profileImageUrl = dto.profileImageUrl ?: "",
            bio = dto.bio ?: "",
            distanceInMeters = dto.distanceInMeters,
            firebaseToken = dto.firebaseToken ?: "",
            longitude = dto.longitude,
            latitude = dto.latitude,
            h3Index = dto.h3Index,
        )
    }

    override fun toDomain(entity: NearbyWorkerEntity): NearbyWorker {
        return NearbyWorker(
            workerId = entity.workerId,
            userId = entity.userId,
            firstName = entity.firstName,
            lastName = entity.lastName,
            profileImageUrl = entity.profileImageUrl,
            bio = entity.bio,
            distanceInMeters = entity.distanceInMeters,
            firebaseToken = entity.firebaseToken,
            longitude = entity.longitude,
            latitude = entity.latitude,
            h3Index = entity.h3Index,
        )
    }
}