package com.example.nexum_cliente.data.category

import com.example.nexum_cliente.common.updateResourceFlow
import com.example.nexum_cliente.data.category.local.CategoryEntity
import com.example.nexum_cliente.data.category.local.CategoryLocalDataSource
import com.example.nexum_cliente.data.category.mapper.CategoryMapper
import com.example.nexum_cliente.data.category.remote.CategoryRemoteDataSource
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.di.modules.IoDispatcher
import com.example.nexum_cliente.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val local: CategoryLocalDataSource,
    private val remote: CategoryRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CategoryRepository {

    override suspend fun updateCategories(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> =
        updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { local.hasResource() },
            remoteCall = { remote.getCategories() },
            saveToCache = { local.replaceAll(CategoryMapper.toEntity(it)) },
            clearCache = { local.clearAll() },
            dispatcher = dispatcher
        )

    override fun observeCategories(): Flow<List<CategoryEntity>> = local.observe()
}