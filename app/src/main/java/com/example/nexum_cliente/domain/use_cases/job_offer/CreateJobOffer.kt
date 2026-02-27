package com.example.nexum_cliente.domain.use_cases.job_offer

import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.repository.JobOfferRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
class CreateJobOffer @Inject constructor(
    private val repository: JobOfferRepository
) {
    operator fun invoke(jobOffer: JobOffer) = repository.createJobOffer(jobOffer)
}