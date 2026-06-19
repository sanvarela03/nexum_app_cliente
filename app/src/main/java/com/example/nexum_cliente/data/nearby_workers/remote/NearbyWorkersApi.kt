package com.example.nexum_cliente.data.nearby_workers.remote

import com.example.nexum_cliente.data.nearby_workers.remote.payload.res.NearbyWorkerRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/28/2026
 * @version 1.0
 */
interface NearbyWorkersApi {
    @GET("/api/v1/job-offers/{jobOfferId}/nearby-workers")
    suspend fun getNearbyWorkers(
        @Path("jobOfferId") jobOfferId: Long,
        @Query("radius-km") radiusKm: Int
    ) : Response<List<NearbyWorkerRes>>
}