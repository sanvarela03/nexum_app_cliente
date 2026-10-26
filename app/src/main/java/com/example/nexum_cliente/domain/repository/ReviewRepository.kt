package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.review.remote.payload.req.CreateReviewReq
import com.example.nexum_cliente.data.review.remote.payload.res.ReviewRes
import com.example.nexum_cliente.data.review.remote.payload.res.UserRatingSummaryRes
import com.example.nexum_cliente.domain.model.PageResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
interface ReviewRepository {
    fun createReview(req: CreateReviewReq): Flow<ApiResponse<ReviewRes>>
    fun getUserRatingSummary(userId: Long): Flow<ApiResponse<UserRatingSummaryRes>>
    fun getReviewsForUser(userId: Long): Flow<ApiResponse<PageResponse<ReviewRes>>>
}
