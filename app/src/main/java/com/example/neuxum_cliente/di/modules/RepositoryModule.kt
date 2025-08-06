package com.example.neuxum_cliente.di.modules

import com.example.neuxum_cliente.data.auth.AuthRepositoryImpl
import com.example.neuxum_cliente.data.auth.remote.AuthApi
import com.example.neuxum_cliente.data.client.ClientRepositoryImpl
import com.example.neuxum_cliente.data.client.local.ClientDao
import com.example.neuxum_cliente.data.client.remote.ClientApi
import com.example.neuxum_cliente.domain.repository.AuthRepository
import com.example.neuxum_cliente.domain.repository.ClientRepository
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
        tokenManager: TokenManager
    ): AuthRepository = AuthRepositoryImpl(api, tokenManager)

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
}