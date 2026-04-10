package com.example.nexum_cliente.data.job_offer.local

import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
@Singleton
class JobOfferLocalDataSource @Inject constructor(
    private val dao: JobOfferDao
) : LocalDataSource<JobOfferEntity> {
    override suspend fun hasResource(): Boolean {
        return dao.getAll().isNotEmpty()
    }

    override suspend fun getAll(): List<JobOfferEntity> {
        return dao.getAll()
    }

    override fun observe(): Flow<List<JobOfferEntity>> {
        return dao.observe()
    }

    override suspend fun replaceAll(resources: List<JobOfferEntity>) {
        dao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}