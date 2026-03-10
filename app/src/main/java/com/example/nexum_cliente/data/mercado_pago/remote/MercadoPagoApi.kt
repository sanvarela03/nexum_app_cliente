package com.example.nexum_cliente.data.mercado_pago.remote

import com.example.nexum_cliente.data.mercado_pago.remote.payload.req.CreatePreferenceRequest
import com.example.nexum_cliente.data.mercado_pago.remote.payload.res.CreatePreferenceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/28/2026
 * @version 1.0
 */
interface MercadoPagoApi {
    @POST("/api/v1/payments/checkout-pro/create-preference")
    suspend fun createPreference(@Body request: CreatePreferenceRequest): Response<CreatePreferenceResponse>
}