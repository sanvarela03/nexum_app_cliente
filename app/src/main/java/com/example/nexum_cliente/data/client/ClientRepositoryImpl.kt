package com.example.nexum_cliente.data.client

import com.example.nexum_cliente.common.updateResourceFlow
import com.example.nexum_cliente.data.client.local.ClientLocalDataSource
import com.example.nexum_cliente.data.client.mapper.ClientMapper
import com.example.nexum_cliente.data.client.remote.ClientRemoteDataSource
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.di.modules.IoDispatcher
import com.example.nexum_cliente.domain.model.Client
import com.example.nexum_cliente.domain.repository.ClientRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Singleton
class ClientRepositoryImpl @Inject constructor(
    private val localDataSource: ClientLocalDataSource,
    private val remoteDataSource: ClientRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ClientRepository {
    override fun observe(): Flow<Client?> =
        localDataSource
            .observe()
            .map { ClientMapper.toDomain(it).firstOrNull() }

    override fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> = updateResourceFlow(
        fetchFromRemote = fetchFromRemote,
        checkCache = { localDataSource.hasResource() },
        remoteCall = { remoteDataSource.getClient() },
        saveToCache = { localDataSource.replaceAll(listOf(ClientMapper.toEntity(it))) },
        clearCache = { localDataSource.clearAll() },
        dispatcher = dispatcher
    )
}
