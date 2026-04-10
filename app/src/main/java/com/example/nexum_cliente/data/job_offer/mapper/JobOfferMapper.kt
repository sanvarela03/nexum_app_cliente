package com.example.nexum_cliente.data.job_offer.mapper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.nexum_cliente.data.job_offer.local.JobOfferEntity
import com.example.nexum_cliente.data.job_offer.remote.payload.req.AddJobOfferReq
import com.example.nexum_cliente.data.job_offer.remote.payload.res.JobOfferRes
import com.example.nexum_cliente.data.mapper.DomainMapper
import com.example.nexum_cliente.data.mapper.EntityMapper
import com.example.nexum_cliente.data.mapper.RequestMapper
import com.example.nexum_cliente.data.mapper.StateToDomainMapper
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.model.Location
import com.example.nexum_cliente.domain.model.params.NewJobOffer
import com.example.nexum_cliente.ui.presenter.job_offer.JobOfferState
import com.example.nexum_cliente.utils.DateUtils

object JobOfferMapper :
    RequestMapper<NewJobOffer, AddJobOfferReq>,
    EntityMapper<JobOfferRes, JobOfferEntity>,
    DomainMapper<JobOfferEntity, JobOffer>,
    StateToDomainMapper<JobOfferState, NewJobOffer> {

    // De Dominio a Petición de Red (para enviar datos)
    override fun toRequest(state: NewJobOffer): AddJobOfferReq {
        return AddJobOfferReq(
            title = state.title,
            description = state.description,
            categoryId = state.categoryId,
            requestedDate = state.requestedDate,
            photos = state.photos,
            location = state.location
        )
    }

    // De Respuesta de Red (DTO) a Entidad de Base de Datos
    override fun toEntity(dto: JobOfferRes): JobOfferEntity {
        return JobOfferEntity(
            id = dto.jobOfferId,
            uuid = dto.jobOfferUuid,
            clientId = dto.clientId,
            title = dto.title,
            description = dto.description,
            categoryId = dto.categoryId,
            categoryName = dto.categoryName,
            h3Idx = dto.h3Idx,
            requestedDate = dto.requiredDate,
            createdAt = dto.createdAt,
            lat = dto.location.lat,
            lng = dto.location.lng,
        )
    }

    // De Entidad de Base de Datos a Modelo de Dominio
    override fun toDomain(entity: JobOfferEntity): JobOffer {
        return JobOffer(
            id = entity.id,
            uuid = entity.uuid,
            clientId = entity.clientId,
            categoryId = entity.categoryId,
            categoryName = entity.categoryName,
            title = entity.title,
            description = entity.description,
            requestedDate = entity.requestedDate,
            h3Idx = entity.h3Idx,
            createdAt = entity.createdAt,
            location = Location(lat = entity.lat, lng = entity.lng),
            photos = emptyList(),
        )
    }

    // De Estado de UI (JobOfferState) a Modelo de Dominio (JobOffer)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun stateToDomain(state: JobOfferState): NewJobOffer {
        Log.d("JobOfferMapper", "Requested date: ${state.requestedDate}")
        Log.d("JobOfferMapper", "Requested time: ${state.requestedTime}")
        val requestedDate = DateUtils.formatToISO8601(state.requestedDate, state.requestedTime)
        Log.d("JobOfferMapper", "Requested date: $requestedDate")
        return NewJobOffer(
            title = state.title,
            description = state.description,
            categoryId = state.categoryId,
            requestedDate = requestedDate,
            photos = state.images.map { it.toString() }, // Convirtiendo Uris a Strings. El UseCase se encargará de subirlas.
            location = listOf(state.longitude, state.latitude) // Cuidado, con este orden
        )
    }


}