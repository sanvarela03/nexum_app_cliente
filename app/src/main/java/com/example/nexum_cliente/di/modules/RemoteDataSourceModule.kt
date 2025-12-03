package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.data.country.remote.CountryApi
import com.example.nexum_cliente.data.country.remote.CountryRemoteDataSource
import dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    @Singleton
    fun provideCountryRemoteDataSource(api: CountryApi): CountryRemoteDataSource =
        CountryRemoteDataSource(api)
}