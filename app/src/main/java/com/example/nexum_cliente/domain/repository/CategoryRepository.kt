package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.category.local.CategoryEntity
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
interface CategoryRepository {
    suspend fun updateCategories(fetchFromRemote: Boolean) : Flow<ApiResponse<Unit>>
    fun observeCategories(): Flow<List<CategoryEntity>>
}