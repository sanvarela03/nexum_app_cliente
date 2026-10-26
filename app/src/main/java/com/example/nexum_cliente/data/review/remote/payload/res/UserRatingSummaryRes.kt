package com.example.nexum_cliente.data.review.remote.payload.res

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class UserRatingSummaryRes(
    val userId: Long,
    val averageRating: Double,
    val totalReviews: Int,
    val stars1: Int = 0,
    val stars2: Int = 0,
    val stars3: Int = 0,
    val stars4: Int = 0,
    val stars5: Int = 0
)
