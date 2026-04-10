package com.example.nexum_cliente.domain.use_cases.job_offer

import com.example.nexum_cliente.domain.repository.JobOfferRepository
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 4/3/2026
 * @version 1.0
 */
@Singleton
class ObserveJobOffers @Inject constructor(
    private val repository: JobOfferRepository
) {
    operator fun invoke() = repository.observeJobOffers()
}