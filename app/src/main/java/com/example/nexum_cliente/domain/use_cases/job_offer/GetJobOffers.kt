package com.example.nexum_cliente.domain.use_cases.job_offer

<<<<<<< Updated upstream
=======
import com.example.nexum_cliente.domain.repository.JobOfferRepository
import javax.inject.Inject
import javax.inject.Singleton

>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class GetJobOffers {
=======
@Singleton
class GetJobOffers @Inject constructor(
    private val repository: JobOfferRepository
) {
    operator fun invoke() = repository.getJobOffers()
>>>>>>> Stashed changes
}