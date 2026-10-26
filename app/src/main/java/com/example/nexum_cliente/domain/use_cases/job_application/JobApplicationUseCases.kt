package com.example.nexum_cliente.domain.use_cases.job_application

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class JobApplicationUseCases @Inject constructor(
    val observeJobApplications: ObserveJobApplications,
    val getJobApplicationsByOffer: GetJobApplicationsByOffer
)

