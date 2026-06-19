package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.model.NearbyWorker
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 5/5/2026
 * @version 1.0
 */
interface NearbyWorkersRepository {
    suspend fun update(
        jobOfferId: Long,
        radiusKm: Int,
        fetchFromRemote: Boolean
    ): Flow<ApiResponse<Unit>>

    fun observe(jobOfferId: Long): Flow<List<NearbyWorker>>
}