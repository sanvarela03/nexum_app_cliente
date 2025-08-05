package com.example.neuxum_cliente.data.client.remote

import com.example.neuxum_cliente.data.client.remote.payload.res.ClientRes
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
interface ClientApi {
    @GET("/api/v1/clients")
    suspend fun getClient(): Response<ClientRes>
}