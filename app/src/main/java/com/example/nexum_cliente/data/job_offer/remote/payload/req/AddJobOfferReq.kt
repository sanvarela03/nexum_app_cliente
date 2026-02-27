package com.example.nexum_cliente.data.job_offer.remote.payload.req

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
data class AddJobOfferReq(
    val title: String,
    val description: String,
    val categoryId: Long,
    val requestedDate: String,
    val photos: List<String>,
    val location: List<Double>,
)
