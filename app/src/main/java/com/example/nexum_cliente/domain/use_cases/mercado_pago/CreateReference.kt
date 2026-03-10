package com.example.nexum_cliente.domain.use_cases.mercado_pago

import com.example.nexum_cliente.data.mercado_pago.remote.payload.req.CreatePreferenceRequest
import com.example.nexum_cliente.domain.repository.MercadoPagoRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/28/2026
 * @version 1.0
 */
class CreateReference @Inject constructor(
    private val mercadoPagoRepository: MercadoPagoRepository
) {
    operator fun invoke(req: CreatePreferenceRequest) = mercadoPagoRepository.createPreference(req)
}