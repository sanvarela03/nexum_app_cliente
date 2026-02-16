package com.example.nexum_cliente.data.job_offer.remote

<<<<<<< Updated upstream
=======
import com.example.nexum_cliente.data.job_offer.remote.payload.req.AddJobOfferReq
import com.example.nexum_cliente.data.job_offer.remote.payload.res.NewJobOfferRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

>>>>>>> Stashed changes
/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
interface JobOfferApi {
<<<<<<< Updated upstream
=======
    @POST("/api/v1/job-offers")
    suspend fun createJobOffer(@Body req: AddJobOfferReq): Response<NewJobOfferRes>
    suspend fun getJobOffers(): Response<List<NewJobOfferRes>>
>>>>>>> Stashed changes
}