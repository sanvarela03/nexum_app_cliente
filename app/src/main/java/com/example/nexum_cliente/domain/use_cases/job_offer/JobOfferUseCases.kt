package com.example.nexum_cliente.domain.use_cases.job_offer

<<<<<<< Updated upstream
data class JobOfferUseCases()
=======
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class JobOfferUseCases @Inject constructor(
    val createJobOffer: CreateJobOffer,
    val getJobOffers: GetJobOffers,
)
>>>>>>> Stashed changes
