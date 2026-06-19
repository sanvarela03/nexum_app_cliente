package com.example.nexum_cliente.data.nearby_workers

import com.example.nexum_cliente.common.updateResourceFlow
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.nearby_workers.local.NearbyWorkersLocalDataSource
import com.example.nexum_cliente.data.nearby_workers.mapper.NearbyWorkerMapper
import com.example.nexum_cliente.data.nearby_workers.remote.NearbyWorkersRemoteDataSource
import com.example.nexum_cliente.di.modules.IoDispatcher
import com.example.nexum_cliente.domain.model.NearbyWorker
import com.example.nexum_cliente.domain.repository.NearbyWorkersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/28/2026
 * @version 1.0
 */
@Singleton
class NearbyWorkersRepositoryImpl @Inject constructor(
    private val localDataSource: NearbyWorkersLocalDataSource,
    private val remoteDataSource: NearbyWorkersRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NearbyWorkersRepository {

    override suspend fun update(
        jobOfferId: Long,
        radiusKm: Int,
        fetchFromRemote: Boolean,

    ): Flow<ApiResponse<Unit>> {
        return updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { localDataSource.hasResource(jobOfferId) },
            remoteCall = { remoteDataSource.getNearbyWorkers(
                jobOfferId = jobOfferId,
                radiusKm = radiusKm
            ) },
            saveToCache = { 
                localDataSource.replaceAll(
                    jobOfferId, 
                    it.map { dto -> NearbyWorkerMapper.toEntityWithOffer(dto, jobOfferId) }
                ) 
            },
            clearCache = { localDataSource.clear(jobOfferId) },
            dispatcher = dispatcher
        )
    }

    override fun observe(jobOfferId: Long): Flow<List<NearbyWorker>> =
        localDataSource.observe(jobOfferId).map { list -> list.map { NearbyWorkerMapper.toDomain(it) } }

}