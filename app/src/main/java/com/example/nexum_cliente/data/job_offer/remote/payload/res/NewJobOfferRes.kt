package com.example.nexum_cliente.data.job_offer.remote.payload.res


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
data class NewJobOfferRes(
    val jobOfferId: Long,
    val jobOfferUuid: String,
    val clientId: Long,
    val categoryId: Long,
)
