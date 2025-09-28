package com.example.neuxum_cliente.data.market_location

import android.util.Log
import com.example.neuxum_cliente.common.apiRequestFlow
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.data.market_location.local.MarketLocationDao
import com.example.neuxum_cliente.data.market_location.local.MarketLocationEntity
import com.example.neuxum_cliente.data.market_location.parser.MarketLocationParser
import com.example.neuxum_cliente.data.market_location.remote.MarketLocationApi
import com.example.neuxum_cliente.di.modules.IoDispatcher
import com.example.neuxum_cliente.domain.repository.MarketLocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 04/09/2025
 * @version 1.0
 */
@Singleton
class MarketLocationRepositoryImpl @Inject constructor (
    private val api: MarketLocationApi,
    private val dao: MarketLocationDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
): MarketLocationRepository {
    override fun observeAllLocations(): Flow<List<MarketLocationEntity>> = dao.observeAllLocations()

    override fun updateAllLocations(countryCode: String, fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> =
        flow {
            // 1) Check local cache synchronously (DB call on IO because flowOn(dispatcher) is applied later)
            val localLocations = dao.getAllLocations()
            val cacheExists = false
            Log.d("SignUpViewModel", "cacheExists=$cacheExists fetchFromRemote=$fetchFromRemote")

            // If cache exists and the caller didn't ask to fetch remote, we return success and stop.
            if (cacheExists && !fetchFromRemote) {
                emit(ApiResponse.Success(Unit))
                return@flow
            }

            // 2) No cache or caller asked for fresh remote data -> call remote
            // Emit Loading if you want the UI to react
            emit(ApiResponse.Loading)

            // The helper apiRequestFlow is assumed to wrap network call and emit ApiResponse<T>
            apiRequestFlow {
                api.getAllLocations(if (countryCode.isBlank()) null else countryCode)
            }.collect { apiRes ->
                when (apiRes) {
                    is ApiResponse.Error -> {
                        // map to your ApiResponse.Error handling (maybe transform)
                        emit(apiRes)
                    }
                    is ApiResponse.Failure -> {
                        emit(ApiResponse.Failure(apiRes.errorMessage, apiRes.code))
                    }
                    ApiResponse.Loading -> {
                        // already handled above but forward if desired
                        emit(ApiResponse.Loading)
                    }
                    is ApiResponse.Success -> {
                        val dto = apiRes.data
                        Log.d("SignUpViewModel", "Remote DTO empty=${dto.isEmpty()}")
                        if (dto.isEmpty()) {
                            dao.clearAll()
                            emit(ApiResponse.Success(Unit))
                        } else {
                            // convert and persist
                            dao.replaceLocations(MarketLocationParser.toEntity(dto))

                            emit(ApiResponse.Success(Unit))
                        }
                    }
                }
            }
        }.flowOn(dispatcher)


    private suspend fun FlowCollector<ApiResponse<Unit>>.checkLocal(
        fetchFromRemote: Boolean
    ) {
        val localLocations = dao.getAllLocations()
        val cacheExists = localLocations.isNotEmpty()
        if (cacheExists && !fetchFromRemote) {
            emit(ApiResponse.Success(Unit))
            return
        }
    }

    private suspend fun FlowCollector<ApiResponse<Unit>>.loadFromRemote() {
        apiRequestFlow {
            api.getAllLocations("")
        }.collect { apiRes ->
            when (apiRes) {
                is ApiResponse.Error -> TODO()
                is ApiResponse.Failure -> {
                    emit(ApiResponse.Failure(apiRes.errorMessage, apiRes.code))
                }

                ApiResponse.Loading -> {
                    emit(ApiResponse.Loading)
                }

                is ApiResponse.Success -> {
                    val dto = apiRes.data
                    Log.d("CategoryRepositoryImpl", "DTO: ${dto.isEmpty()}")
                    if (dto.isEmpty()) {
                        Log.d("CategoryRepositoryImpl", "Empty response from server")
                        dao.clearAll()
                        emit(ApiResponse.Success(Unit))
                        // emit(ApiResponse.Error("Empty response from server"))
                    } else {
                        dao.replaceLocations(MarketLocationParser.toEntity(dto))
                        emit(ApiResponse.Success(Unit))
                    }
                }
            }
        }
    }
}