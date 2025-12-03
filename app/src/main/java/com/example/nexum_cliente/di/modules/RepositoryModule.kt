package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.data.auth.AuthRepositoryImpl
import com.example.nexum_cliente.data.auth.remote.AuthApi
import com.example.nexum_cliente.data.category.CategoryRepositoryImpl
import com.example.nexum_cliente.data.category.local.CategoryDao
import com.example.nexum_cliente.data.category.remote.CategoryApi
import com.example.nexum_cliente.data.client.ClientRepositoryImpl
import com.example.nexum_cliente.data.client.local.ClientDao
import com.example.nexum_cliente.data.client.remote.ClientApi
import com.example.nexum_cliente.data.country.CountryRepositoryImpl
import com.example.nexum_cliente.data.country.local.CountryDao
import com.example.nexum_cliente.data.country.local.CountryLocalDataSource
import com.example.nexum_cliente.data.country.remote.CountryApi
import com.example.nexum_cliente.data.country.remote.CountryRemoteDataSource
import com.example.nexum_cliente.data.market_location.MarketLocationRepositoryImpl
import com.example.nexum_cliente.data.market_location.local.MarketLocationDao
import com.example.nexum_cliente.data.market_location.remote.MarketLocationApi
import com.example.nexum_cliente.domain.repository.AuthRepository
import com.example.nexum_cliente.domain.repository.CategoryRepository
import com.example.nexum_cliente.domain.repository.ClientRepository
import com.example.nexum_cliente.domain.repository.CountryRepository
import com.example.nexum_cliente.domain.repository.MarketLocationRepository
import com.example.protapptest.security.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/1/2025
 * @version 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        tokenManager: TokenManager,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): AuthRepository = AuthRepositoryImpl(api, tokenManager, dispatcher)

    @Provides
    @Singleton
    fun provideClientRepository(
        clientApi: ClientApi,
        clientDao: ClientDao,
        tokenManager: TokenManager,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ClientRepository = ClientRepositoryImpl(
        clientApi = clientApi,
        clientDao = clientDao,
        tokenManager = tokenManager,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryApi: CategoryApi,
        categoryDao: CategoryDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CategoryRepository = CategoryRepositoryImpl(
        categoryApi = categoryApi,
        categoryDao = categoryDao,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideMarketLocationRepository(
        marketLocationApi: MarketLocationApi,
        marketLocationDao: MarketLocationDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): MarketLocationRepository = MarketLocationRepositoryImpl(
        api = marketLocationApi,
        dao = marketLocationDao,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideCountryRepository(
        localDataSource: CountryLocalDataSource,
        remoteDataSource: CountryRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CountryRepository = CountryRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        dispatcher = dispatcher
    )


}