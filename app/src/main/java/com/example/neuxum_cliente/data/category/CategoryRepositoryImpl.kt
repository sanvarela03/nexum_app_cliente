package com.example.neuxum_cliente.data.category

import android.util.Log
import com.example.neuxum_cliente.common.apiRequestFlow
import com.example.neuxum_cliente.data.category.local.CategoryDao
import com.example.neuxum_cliente.data.category.local.CategoryEntity
import com.example.neuxum_cliente.data.category.parser.CategoryParser
import com.example.neuxum_cliente.data.category.remote.CategoryApi
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.di.modules.IoDispatcher
import com.example.neuxum_cliente.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private val categoryApi: CategoryApi,
    private val categoryDao: CategoryDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CategoryRepository {
    override fun observeCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.observeCategories()
    }

    override suspend fun updateCategories(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> =
        flow {
            checkLocal(fetchFromRemote)
            loadFromRemote()
        }.flowOn(dispatcher)

    private suspend fun FlowCollector<ApiResponse<Unit>>.loadFromRemote() {
        apiRequestFlow {
            categoryApi.getCategories()
        }.collect { apiRes ->
            when (apiRes) {
                is ApiResponse.Error -> TODO()
                is ApiResponse.Failure -> {
                    emit(ApiResponse.Failure(apiRes.errorMessage, apiRes.code))
                }

                ApiResponse.Loading -> {
                    emit(ApiResponse.Loading)
                }

                is ApiResponse.Success -> {
                    val dto = apiRes.data
                    Log.d("CategoryRepositoryImpl", "DTO: ${dto.isEmpty()}")
                    if (dto.isEmpty()) {
                        Log.d("CategoryRepositoryImpl", "Empty response from server")
                        categoryDao.clearAll()
                        emit(ApiResponse.Success(Unit))
                        // emit(ApiResponse.Error("Empty response from server"))
                    } else {
                        categoryDao.replaceCategories(CategoryParser.toEntity(dto))
                        emit(ApiResponse.Success(Unit))
                    }
                }
            }
        }
    }

    private suspend fun FlowCollector<ApiResponse<Unit>>.checkLocal(
        fetchFromRemote: Boolean
    ) {
        val localCategories = categoryDao.getAllCategories()
        val cacheExists = localCategories.isNotEmpty()
        if (cacheExists && !fetchFromRemote) {
            emit(ApiResponse.Success(Unit))
            return
        }
    }

}