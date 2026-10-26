package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.model.JobApplication
import kotlinx.coroutines.flow.Flow

interface JobApplicationRepository {
    fun getJobApplicationsByOffer(offerIdentifier: String): Flow<ApiResponse<List<JobApplication>>>
}
