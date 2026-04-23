package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.utils.location.FusedLocationClientImpl
import com.example.nexum_cliente.utils.location.LocationClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    @Singleton
    abstract fun bindLocationClient(fusedLocationClientImpl: FusedLocationClientImpl): LocationClient
}
