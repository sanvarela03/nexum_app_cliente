package com.example.nexum_cliente.domain.use_cases.mercado_pago

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class MercadoPagoUseCases @Inject constructor(
    val createPreference: CreateReference,
)
