package com.example.nexum_cliente.domain.use_cases.review

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.review.remote.payload.res.ReviewRes
import com.example.nexum_cliente.domain.model.PageResponse
import com.example.nexum_cliente.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 10/5/2026
 * @version 1.0
 */
class GetReviewsForUserUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    operator fun invoke(userId: Long): Flow<ApiResponse<PageResponse<ReviewRes>>> =
        repository.getReviewsForUser(userId)
}
