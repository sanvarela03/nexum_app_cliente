package com.example.nexum_cliente.data.mercado_pago.remote.payload.req

data class CreatePreferenceRequest(
    val title: String,
    val quantity: Int,
    val unitPrice: Double
)
