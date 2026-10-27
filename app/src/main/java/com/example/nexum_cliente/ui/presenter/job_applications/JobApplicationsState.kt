package com.example.nexum_cliente.ui.presenter.job_applications

import com.example.nexum_cliente.domain.model.JobApplication

data class JobApplicationsState(
    val isLoading: Boolean = false,
    val jobApplications: List<JobApplication> = emptyList(),
    val existingContractApplicationIds: Set<Long> = emptySet(),
    val error: String? = null
)
