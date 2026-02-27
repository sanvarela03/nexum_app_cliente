package com.example.nexum_cliente.data.job_offer.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nexum_cliente.data.job_offer.local.JobOfferEntity
import com.example.nexum_cliente.data.job_offer.remote.payload.req.AddJobOfferReq
import com.example.nexum_cliente.data.job_offer.remote.payload.res.NewJobOfferRes
import com.example.nexum_cliente.data.mapper.DomainMapper
import com.example.nexum_cliente.data.mapper.EntityMapper
import com.example.nexum_cliente.data.mapper.RequestMapper
import com.example.nexum_cliente.data.mapper.StateToDomainMapper
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.ui.presenter.job_offer.JobOfferState
import com.example.nexum_cliente.utils.DateUtils

object JobOfferMapper :
    RequestMapper<JobOffer, AddJobOfferReq>,
    EntityMapper<NewJobOfferRes, JobOfferEntity>, DomainMapper<JobOfferEntity, JobOffer>,
    StateToDomainMapper<JobOfferState, JobOffer> {

    // De Dominio a Petición de Red (para enviar datos)
    override fun toRequest(state: JobOffer): AddJobOfferReq {
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
    override fun toEntity(dto: NewJobOfferRes): JobOfferEntity {
        return JobOfferEntity(
            id = dto.jobOfferId,
            title = "", // El DTO de respuesta no proporciona estos datos
            description = "",
            categoryId = dto.categoryId,
            requestedDate = ""
        )
    }

    // De Entidad de Base de Datos a Modelo de Dominio
    override fun toDomain(entity: JobOfferEntity): JobOffer {
        return JobOffer(
            title = entity.title,
            description = entity.description,
            categoryId = entity.categoryId,
            requestedDate = entity.requestedDate,
            photos = emptyList(), // La entidad no tiene fotos, se devuelve una lista vacía
            location = emptyList() // La entidad no tiene ubicación, se devuelve una lista vacía
        )
    }

    // De Estado de UI (JobOfferState) a Modelo de Dominio (JobOffer)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun stateToDomain(state: JobOfferState): JobOffer {
        return JobOffer(
            title = state.title,
            description = state.description,
            categoryId = state.categoryId,
            requestedDate = DateUtils.formatToISO8601(state.requestedDate, state.requestedTime),
            photos = state.images.map { it.toString() }, // Convirtiendo Uris a Strings. El UseCase se encargará de subirlas.
            location = listOf(state.longitude, state.latitude) // Cuidado, con este orden
        )
    }
}