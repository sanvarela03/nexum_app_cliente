package com.example.nexum_cliente.domain.use_cases.category

import com.example.nexum_cliente.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
class UpdateCategories @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(fetchFromRemote: Boolean) =
        categoryRepository.updateCategories(fetchFromRemote)

}