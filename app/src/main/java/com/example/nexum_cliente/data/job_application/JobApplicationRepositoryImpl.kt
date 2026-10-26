package com.example.nexum_cliente.data.job_application

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.job_application.mapper.toDomain
import com.example.nexum_cliente.data.job_application.remote.JobApplicationRemoteDataSource
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.domain.repository.JobApplicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobApplicationRepositoryImpl @Inject constructor(
    private val remoteDataSource: JobApplicationRemoteDataSource
) : JobApplicationRepository {
    override fun getJobApplicationsByOffer(offerIdentifier: String): Flow<ApiResponse<List<JobApplication>>> {
        return remoteDataSource.getJobApplicationsByOffer(offerIdentifier).map { response ->
            when (response) {
                is ApiResponse.Success -> ApiResponse.Success(response.data.toDomain(offerIdentifier))
                is ApiResponse.Error -> ApiResponse.Error(response.errorMessage)
                is ApiResponse.Failure -> ApiResponse.Failure(response.errorMessage, response.code)
                ApiResponse.Loading -> ApiResponse.Loading
            }
        }
    }
}
