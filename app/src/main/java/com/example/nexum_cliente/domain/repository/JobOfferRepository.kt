package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.job_offer.remote.payload.res.NewJobOfferRes
import com.example.nexum_cliente.domain.model.JobOffer
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
interface JobOfferRepository {
    fun createJobOffer(jobOffer: JobOffer): Flow<ApiResponse<NewJobOfferRes>>
    fun getJobOffers(): Flow<List<JobOffer>>
}
