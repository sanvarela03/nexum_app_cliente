package com.example.nexum_cliente.data.review

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.review.remote.ReviewRemoteDataSource
import com.example.nexum_cliente.data.review.remote.payload.req.CreateReviewReq
import com.example.nexum_cliente.data.review.remote.payload.res.ReviewRes
import com.example.nexum_cliente.data.review.remote.payload.res.UserRatingSummaryRes
import com.example.nexum_cliente.domain.model.PageResponse
import com.example.nexum_cliente.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
@Singleton
class ReviewRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) : ReviewRepository {

    override fun createReview(req: CreateReviewReq): Flow<ApiResponse<ReviewRes>> =
        remoteDataSource.createReview(req)

    override fun getUserRatingSummary(userId: Long): Flow<ApiResponse<UserRatingSummaryRes>> =
        remoteDataSource.getUserRatingSummary(userId)

    override fun getReviewsForUser(userId: Long): Flow<ApiResponse<PageResponse<ReviewRes>>> =
        remoteDataSource.getReviewsForUser(userId)
}
