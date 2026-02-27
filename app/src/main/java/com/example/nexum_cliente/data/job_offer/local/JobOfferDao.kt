package com.example.nexum_cliente.data.job_offer.local

import androidx.room.Dao
import androidx.room.Query
import com.example.nexum_cliente.data.local_storage.BaseDao
import com.example.nexum_cliente.data.local_storage.ReadableDao
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
@Dao
interface JobOfferDao : BaseDao<JobOfferEntity>, ReadableDao<JobOfferEntity, Long> {
    @Query("SELECT * FROM job_offers")
    override suspend fun getAll(): List<JobOfferEntity>

    override suspend fun getById(id: Long): JobOfferEntity? {
        TODO("Not yet implemented")
    }

    override fun observe(): Flow<List<JobOfferEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

    override suspend fun replaceAll(entities: List<JobOfferEntity>) {
        TODO("Not yet implemented")
    }
}