package com.example.nexum_cliente.data.job_offer.remote

<<<<<<< Updated upstream
=======
import android.util.Log
import com.example.nexum_cliente.common.apiRequestFlow
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.job_offer.remote.payload.req.AddJobOfferReq
import com.example.nexum_cliente.data.job_offer.remote.payload.res.NewJobOfferRes
import kotlinx.coroutines.flow.Flow
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
class JobOfferRemoteDataSource {
=======
@Singleton
class JobOfferRemoteDataSource @Inject constructor(
    private val api: JobOfferApi
) {
    fun createJobOffer(req: AddJobOfferReq): Flow<ApiResponse<NewJobOfferRes>> =
        apiRequestFlow {
            Log.d("JobOfferRemoteDataSource", "createJobOffer: $req")
            api.createJobOffer(req)
        }
>>>>>>> Stashed changes
}