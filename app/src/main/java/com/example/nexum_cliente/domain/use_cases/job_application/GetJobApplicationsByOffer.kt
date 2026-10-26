package com.example.nexum_cliente.domain.use_cases.job_application

import com.example.nexum_cliente.domain.repository.JobApplicationRepository
import javax.inject.Inject

class GetJobApplicationsByOffer @Inject constructor(
    private val repository: JobApplicationRepository
) {
    operator fun invoke(offerIdentifier: String) = repository.getJobApplicationsByOffer(offerIdentifier)
}
