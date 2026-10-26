package com.example.nexum_cliente.data.contract.remote.payload.res

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class ContractRes(
    val id: Long,
    val jobApplicationId: Long,
    val agreedFormOfPaymentId: Long?,
    val agreedFormOfPaymentName: String?,
    val agreedAmount: Double,
    val currencyCode: String,
    val currentStatus: String,
    val statusDescription: String?,
    val meetingDate: String?,
    val meetingH3Index: String?,
    val verificationCode: String?,
    val verificationExpiresAt: String?,
    val verifiedAt: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val workerId: Long? = null,
    val workerUserId: Long? = null,
    val workerName: String? = null,
    val workerImageUrl: String? = null,
    val clientId: Long? = null,
    val clientUserId: Long? = null,
    val clientName: String? = null,
    val clientImageUrl: String? = null,
    val jobOfferId: Long? = null,
    val jobOfferTitle: String? = null
)
