package com.example.neuxum_cliente.data.category.remote

import com.example.neuxum_cliente.data.category.remote.payload.res.CategoryRes
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
interface CategoryApi {

    @GET("/api/v1/categories")
    suspend fun getCategories() : Response<List<CategoryRes>>
}