package com.example.nexum_cliente.data.job_offer

<<<<<<< Updated upstream
=======
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.job_offer.local.JobOfferLocalDataSource
import com.example.nexum_cliente.data.job_offer.mapper.JobOfferMapper
import com.example.nexum_cliente.data.job_offer.remote.JobOfferRemoteDataSource
import com.example.nexum_cliente.data.job_offer.remote.payload.res.NewJobOfferRes
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.repository.JobOfferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class JobOfferRepositoryImpl {
=======
class JobOfferRepositoryImpl @Inject constructor(
    private val remoteDataSource: JobOfferRemoteDataSource,
    private val localDataSource: JobOfferLocalDataSource
) : JobOfferRepository {
    override fun createJobOffer(jobOffer: JobOffer): Flow<ApiResponse<NewJobOfferRes>> =
        remoteDataSource.createJobOffer(JobOfferMapper.toRequest(jobOffer))

    override fun getJobOffers(): Flow<List<JobOffer>> {
        return flow {
        }
    }

>>>>>>> Stashed changes
}