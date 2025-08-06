package com.example.neuxum_cliente.di.modules

import com.example.neuxum_cliente.data.category.local.CategoryDao
import com.example.neuxum_cliente.data.client.local.ClientDao
import com.example.neuxum_cliente.data.client.local.db.ClientDb
import com.example.neuxum_cliente.data.client.local.role.RoleDao
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


}