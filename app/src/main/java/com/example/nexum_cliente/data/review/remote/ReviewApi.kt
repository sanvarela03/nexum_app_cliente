package com.example.nexum_cliente.data.review.remote

import com.example.nexum_cliente.data.review.remote.payload.req.CreateReviewReq
import com.example.nexum_cliente.data.review.remote.payload.res.ReviewRes
import com.example.nexum_cliente.data.review.remote.payload.res.UserRatingSummaryRes
import com.example.nexum_cliente.domain.model.PageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
interface ReviewApi {

    @POST("/api/v1/reviews")
    suspend fun createReview(@Body req: CreateReviewReq): Response<ReviewRes>

    @GET("/api/v1/reviews/users/{userId}/summary")
    suspend fun getUserRatingSummary(@Path("userId") userId: Long): Response<UserRatingSummaryRes>

    @GET("/api/v1/reviews/users/{userId}")
    suspend fun getReviewsForUser(
        @Path("userId") userId: Long,
        @Query("size") size: Int = 10
    ): Response<PageResponse<ReviewRes>>
}
