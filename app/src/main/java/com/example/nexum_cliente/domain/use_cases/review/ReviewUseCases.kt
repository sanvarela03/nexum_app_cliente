package com.example.nexum_cliente.domain.use_cases.review

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
data class ReviewUseCases @Inject constructor(
    val createReview: CreateReviewUseCase,
    val getUserRatingSummary: GetUserRatingSummaryUseCase,
    val getReviewsForUser: GetReviewsForUserUseCase
)
