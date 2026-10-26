package com.example.nexum_cliente.data.review.remote.payload.req

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class CreateReviewReq(
    val contractId: Long,
    val rating: Int,
    val comment: String? = null
)
