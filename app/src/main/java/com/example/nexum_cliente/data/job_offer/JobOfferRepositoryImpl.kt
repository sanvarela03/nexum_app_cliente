package com.example.nexum_cliente.data.job_offer

import com.example.nexum_cliente.common.updateResourceFlow
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.job_offer.local.JobOfferLocalDataSource
import com.example.nexum_cliente.data.job_offer.mapper.JobOfferMapper
import com.example.nexum_cliente.data.job_offer.remote.JobOfferRemoteDataSource
import com.example.nexum_cliente.data.job_offer.remote.payload.res.NewJobOfferRes
import com.example.nexum_cliente.di.modules.IoDispatcher
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.model.params.NewJobOffer
import com.example.nexum_cliente.domain.repository.JobOfferRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
@Singleton
class JobOfferRepositoryImpl @Inject constructor(
    private val remote: JobOfferRemoteDataSource,
    private val local: JobOfferLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : JobOfferRepository {
    override fun updateJobOffers(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> =
        updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { local.hasResource() },
            remoteCall = { remote.getJobOffers() },
            saveToCache = { local.replaceAll(JobOfferMapper.toEntity(it)) },
            clearCache = { local.clearAll() },
            dispatcher = dispatcher
        )

    override fun observeJobOffers(): Flow<List<JobOffer>> =
        local.observe().map { it -> it.map { JobOfferMapper.toDomain(it) } }

    override fun createJobOffer(newJobOffer: NewJobOffer): Flow<ApiResponse<NewJobOfferRes>> =
        remote.createJobOffer(JobOfferMapper.toRequest(newJobOffer))

    override fun getJobOffers(): Flow<List<JobOffer>> {
        return flow {
            // TODO: Implementar lógica para obtener las ofertas de trabajo desde el servidor
        }
    }
}