package com.example.neuxum_cliente.di

import com.example.neuxum_cliente.data.market_location.local.MarketLocationDao
import com.example.neuxum_cliente.data.market_location.remote.FakeMarketLocationApi
import com.example.neuxum_cliente.data.market_location.remote.MarketLocationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the fake MarketLocationApi that reads from Room.
 * Put this module in `debug` source set if you only want it for debug builds.
 */
@Module
@InstallIn(SingletonComponent::class)
object FakeNetworkModule {

}
