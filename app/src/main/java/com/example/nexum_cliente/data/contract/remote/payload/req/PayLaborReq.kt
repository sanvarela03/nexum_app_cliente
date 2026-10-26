package com.example.nexum_cliente.data.contract.remote.payload.req

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
data class PayLaborReq(
    val externalPaymentId: Long? = null,
    val paymentAmount: Double? = null,
    val paymentMethod: String? = "credit_card",
    val paymentProvider: String? = "MERCADO_PAGO"
)
