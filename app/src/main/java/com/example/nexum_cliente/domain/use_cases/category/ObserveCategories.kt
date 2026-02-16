package com.example.nexum_cliente.domain.use_cases.category

import com.example.nexum_cliente.data.category.local.CategoryEntity
import com.example.nexum_cliente.domain.repository.CategoryRepository
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
class ObserveCategories @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryEntity>> {
        return categoryRepository.observeCategories()
    }
}