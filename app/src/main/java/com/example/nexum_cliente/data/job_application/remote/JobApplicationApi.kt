package com.example.nexum_cliente.data.job_application.remote

import com.example.nexum_cliente.data.job_application.remote.payload.res.JobApplicationRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface JobApplicationApi {
    @GET("/api/v1/job-applications/by-offer/{offerIdentifier}")
    suspend fun getJobApplicationsByOffer(
        @Path("offerIdentifier") offerIdentifier: String
    ): Response<List<JobApplicationRes>>
}
