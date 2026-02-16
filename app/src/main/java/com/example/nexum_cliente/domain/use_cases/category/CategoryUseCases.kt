package com.example.nexum_cliente.domain.use_cases.category

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
data class CategoryUseCases @Inject constructor(
    val updateCategories: UpdateCategories,
    val observeCategories: ObserveCategories
)