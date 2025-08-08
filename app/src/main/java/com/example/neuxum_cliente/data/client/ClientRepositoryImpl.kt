package com.example.neuxum_cliente.data.client

import android.util.Log
import com.example.neuxum_cliente.common.apiRequestFlow
import com.example.neuxum_cliente.data.client.local.ClientDao
import com.example.neuxum_cliente.data.client.local.ClientEntity
import com.example.neuxum_cliente.data.client.parser.ClientParser
import com.example.neuxum_cliente.data.client.remote.ClientApi
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.di.modules.IoDispatcher
import com.example.neuxum_cliente.domain.repository.ClientRepository
import com.example.protapptest.security.TokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private val clientApi: ClientApi,
    private val clientDao: ClientDao,
    private val tokenManager: TokenManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ClientRepository {

    override fun observeClient(userId: Long?): Flow<ClientEntity?> {
        return clientDao.observeClientByUserId(userId)
    }

    override fun observeUserId(): Flow<Long?> {
        return tokenManager.getUserId()
    }

    override fun updateClient(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading)

        // 1. Intenta leer de la caché
        val currentUserId = tokenManager.getUserId().firstOrNull()
        if (currentUserId == null) {
            emit(ApiResponse.Error("No user id found"))
            return@flow
        }

        val localClient = clientDao.getClientByUserId(currentUserId)
        val cacheExists = localClient != null

        // 2. Si no pide remoto y hay caché, terminamos
        if (cacheExists && !fetchFromRemote) {
            emit(ApiResponse.Success(Unit))
            return@flow
        }

        // 3. Llamada a API y mapeo
        apiRequestFlow { clientApi.getClient() }
            .collect { apiRes ->
                when (apiRes) {
                    is ApiResponse.Loading -> {
                        emit(ApiResponse.Loading)
                    }

                    is ApiResponse.Failure -> {
                        emit(ApiResponse.Failure(apiRes.errorMessage, apiRes.code))
                    }

                    is ApiResponse.Error -> {
                        emit(ApiResponse.Error(apiRes.errorMessage))
                    }

                    is ApiResponse.Success -> {
                        val dto = apiRes.data
                        Log.d("ClientRepositoryImpl", "DTO: $dto")
                        if (dto == null) {
                            emit(ApiResponse.Error("Empty response from server"))
                        } else {
                            // 4. Transacción: borrar + guardar
                            clientDao.replaceClient(ClientParser.toEntity(dto))
                            emit(ApiResponse.Success(Unit))
                        }
                    }
                }
            }
    }.flowOn(dispatcher)
}
