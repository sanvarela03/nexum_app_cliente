package com.example.nexum_cliente.data.nearby_workers.local

import kotlinx.coroutines.flow.Flow
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
class NearbyWorkersLocalDataSource @Inject constructor(
    private val dao: NearbyWorkerDao
) {
    suspend fun hasResource(jobOfferId: Long): Boolean {
        return dao.getAllByJobOffer(jobOfferId).isNotEmpty()
    }

    suspend fun getAll(jobOfferId: Long): List<NearbyWorkerEntity> {
        return dao.getAllByJobOffer(jobOfferId)
    }

    fun observe(jobOfferId: Long): Flow<List<NearbyWorkerEntity>> {
        return dao.observeByJobOffer(jobOfferId)
    }

    suspend fun replaceAll(jobOfferId: Long, resources: List<NearbyWorkerEntity>) {
        dao.replaceAllByJobOffer(jobOfferId, resources)
    }

    suspend fun clear(jobOfferId: Long) {
        dao.clearByJobOffer(jobOfferId)
    }
}