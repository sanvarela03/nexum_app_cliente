package com.example.neuxum_cliente.domain.use_cases.category

import com.example.neuxum_cliente.data.category.local.CategoryEntity
import com.example.neuxum_cliente.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
class ObserveCategories(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryEntity>> {
        return categoryRepository.observeCategories()
    }

}