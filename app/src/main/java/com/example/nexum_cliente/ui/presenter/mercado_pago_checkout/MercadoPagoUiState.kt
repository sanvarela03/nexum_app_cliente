package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import com.example.nexum_cliente.data.mercado_pago.remote.payload.req.CreatePreferenceRequest

data class MercadoPagoUiState(
    val title: String = "",
    val price: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
    val preferenceId: String? = null,
    val error: String? = null,
    val paymentResult: PaymentResult? = null,
    val initPoint: String? = null
) {
    fun isValid(): Boolean {
        return title.isNotEmpty() && price.isNotEmpty() && quantity.isNotEmpty()
    }

    fun toReq(): CreatePreferenceRequest {
        return CreatePreferenceRequest(
            title = title,
            quantity = quantity.toInt(),
            unitPrice = price.toDoubleOrNull() ?: 0.0
        )
    }
}

sealed class PaymentResult {
    data class Success(val paymentId: String) : PaymentResult()
    object Failure : PaymentResult()
    object Pending : PaymentResult()
}
