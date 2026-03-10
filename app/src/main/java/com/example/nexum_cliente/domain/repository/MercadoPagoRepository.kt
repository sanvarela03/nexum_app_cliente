package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.mercado_pago.remote.payload.req.CreatePreferenceRequest
import com.example.nexum_cliente.data.mercado_pago.remote.payload.res.CreatePreferenceResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/28/2026
 * @version 1.0
 */
interface MercadoPagoRepository {
    fun createPreference(req: CreatePreferenceRequest): Flow<ApiResponse<CreatePreferenceResponse>>
}