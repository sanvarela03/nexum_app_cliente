package com.example.nexum_cliente.data.category.remote

import com.example.nexum_cliente.common.apiRequestFlow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
@Singleton
class CategoryRemoteDataSource @Inject constructor(
    private val categoryApi: CategoryApi
) {
    fun getCategories() = apiRequestFlow { categoryApi.getCategories() }
}