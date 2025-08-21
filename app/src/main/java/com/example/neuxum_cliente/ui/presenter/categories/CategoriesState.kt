package com.example.neuxum_cliente.ui.presenter.categories

import com.example.neuxum_cliente.data.category.local.CategoryEntity


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
data class CategoriesState(
    val categories: List<CategoryEntity> = emptyList(),
    var errorMessage: String = "",
    val isRefreshing: Boolean = false,
)