package com.example.nexum_cliente.data.nearby_workers.remote

import com.example.nexum_cliente.common.apiRequestFlow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 5/5/2026
 * @version 1.0
 */
@Singleton
class NearbyWorkersRemoteDataSource @Inject constructor(
    private val nearbyWorkersApi: NearbyWorkersApi
) {
    fun getNearbyWorkers(jobOfferId: Long, radiusKm: Int) =
        apiRequestFlow { nearbyWorkersApi.getNearbyWorkers(jobOfferId, radiusKm) }
}