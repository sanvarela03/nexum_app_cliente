package com.example.nexum_cliente.data.contract.remote.payload.req

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class NewContractReq(
    val jobApplicationId: Long,
    val formOfPaymentId: Long = 1L,
    val agreedAmount: Double,
    val currencyCode: String = "COP",
    val externalPaymentId: Long? = null,
    val paymentAmount: Double? = null,
    val paymentMethod: String? = null,
    val paymentProvider: String? = "MERCADO_PAGO"
)
