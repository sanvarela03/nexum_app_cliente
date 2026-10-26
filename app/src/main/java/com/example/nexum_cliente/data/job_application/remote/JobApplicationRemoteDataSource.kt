package com.example.nexum_cliente.data.job_application.remote

import com.example.nexum_cliente.common.apiRequestFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobApplicationRemoteDataSource @Inject constructor(
    private val api: JobApplicationApi
) {
    fun getJobApplicationsByOffer(offerIdentifier: String) =
        apiRequestFlow { api.getJobApplicationsByOffer(offerIdentifier) }
}
