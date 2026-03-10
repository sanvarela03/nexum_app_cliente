package com.example.nexum_cliente.data.profile.remote

import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
interface ProfileApi {
    @GET("/api/v1/categories")
    suspend fun getCategories() : Response<List<CategoryRes>>
}