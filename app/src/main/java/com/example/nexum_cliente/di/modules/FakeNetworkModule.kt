package com.example.nexum_cliente.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds the fake MarketLocationApi that reads from Room.
 * Put this module in `debug` source set if you only want it for debug builds.
 */
@Module
@InstallIn(SingletonComponent::class)
object FakeNetworkModule {

}
