package com.example.neuxum_cliente.domain.repository

import com.example.neuxum_cliente.data.category.local.CategoryEntity
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
interface CategoryRepository {
    fun observeCategories(): Flow<List<CategoryEntity>>
    suspend fun updateCategories(fetchFromRemote: Boolean) : Flow<ApiResponse<Unit>>
}