package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.data.category.local.CategoryDao
import com.example.nexum_cliente.data.client.local.ClientDao
import com.example.nexum_cliente.data.client.local.db.ClientDb
import com.example.nexum_cliente.data.client.local.role.RoleDao
import com.example.nexum_cliente.data.country.local.CountryDao
import com.example.nexum_cliente.data.market_location.local.MarketLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideClientDao(clientDb: ClientDb): ClientDao = clientDb.clientDao

    @Provides
    @Singleton
    fun provideRoleDao(clientDb: ClientDb): RoleDao = clientDb.roleDao

    @Provides
    @Singleton
    fun provideCategoryDao(clientDb: ClientDb): CategoryDao = clientDb.categoryDao

    @Provides
    @Singleton
    fun provideMarketLocationDao(clientDb: ClientDb): MarketLocationDao = clientDb.marketLocationDao

    @Provides
    @Singleton
    fun provideCountryDao(clientDb: ClientDb): CountryDao = clientDb.countryDao

}