package com.example.nexum_cliente.data.country

import com.example.nexum_cliente.common.updateResourceFlow
import com.example.nexum_cliente.data.country.local.CountryEntity
import com.example.nexum_cliente.data.country.local.CountryLocalDataSource
import com.example.nexum_cliente.data.country.remote.CountryRemoteDataSource
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.di.modules.IoDispatcher
import com.example.nexum_cliente.domain.repository.CountryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
@Singleton
class CountryRepositoryImpl @Inject constructor(
    private val localDataSource: CountryLocalDataSource,
    private val remoteDataSource: CountryRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CountryRepository {
    override fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        return updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { localDataSource.hasCountries() },
            remoteCall = { remoteDataSource.getCountries() },
            saveToCache = { localDataSource.replaceCountries(it) },
            clearCache = { localDataSource.clearAll() },
            dispatcher = dispatcher
        )
    }
    override fun observe(): Flow<List<CountryEntity>> = localDataSource.observeCountries()

}