package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.data.country.local.CountryDao
import com.example.nexum_cliente.data.country.local.CountryLocalDataSource
import dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideCountryLocalDataSource(countryDao: CountryDao): CountryLocalDataSource =
        CountryLocalDataSource(countryDao)
}