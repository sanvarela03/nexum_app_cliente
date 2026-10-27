package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.data.mercado_pago.remote.payload.req.CreatePreferenceRequest

data class MercadoPagoUiState(
    val title: String = "Tarifa de seguridad y confianza",
    val price: String = "8700",
    val quantity: String = "1",
    val isLoading: Boolean = false,
    val preferenceId: String? = null,
    val error: String? = null,
    val paymentResult: PaymentResult? = null,
    val initPoint: String? = null,
    val jobApplicationId: Long? = null,
    val agreedAmount: Double? = null,
    val isCreatingContract: Boolean = false,
    val createdContract: ContractRes? = null,
    val hasExistingContract: Boolean = false,
    val isCheckingExistingContract: Boolean = false,
    val paymentType: String? = null
) {
    val isContractEstablished: Boolean
        get() = createdContract != null || hasExistingContract

    val isPaymentSuccess: Boolean
        get() = paymentResult is PaymentResult.Success

    val isPaymentOrContractDone: Boolean
        get() = isContractEstablished || isPaymentSuccess

    fun isValid(): Boolean {
        return title.isNotEmpty() && price.isNotEmpty() && quantity.isNotEmpty()
    }

    fun toReq(): CreatePreferenceRequest {
        val extRef = if (jobApplicationId != null) {
            val amountStr = agreedAmount?.toLong()?.toString() ?: "0"
            "${jobApplicationId}_$amountStr"
        } else null

        return CreatePreferenceRequest(
            title = title,
            quantity = quantity.toInt(),
            unitPrice = price.toDoubleOrNull() ?: 0.0,
            externalReference = extRef
        )
    }
}

sealed class PaymentResult {
    data class Success(val paymentId: String) : PaymentResult()
    object Failure : PaymentResult()
    object Pending : PaymentResult()
}
