package com.example.nexum_cliente.data.job_offer.local

import androidx.room.Dao
import androidx.room.Query
import com.example.nexum_cliente.data.local_storage.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
@Dao
interface JobOfferDao : BaseDao<JobOfferEntity> {
    companion object {
        const val TABLE_NAME = "job_offers"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAll(): List<JobOfferEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getById(id: Long): JobOfferEntity?

    @Query("SELECT * FROM $TABLE_NAME")
    fun observe(): Flow<List<JobOfferEntity>>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearAll()

    suspend fun replaceAll(entities: List<JobOfferEntity>) {
        clearAll()
        insertAll(entities)
    }
}