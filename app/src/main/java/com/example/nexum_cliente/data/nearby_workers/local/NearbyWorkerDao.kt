package com.example.nexum_cliente.data.nearby_workers.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 5/5/2026
 * @version 1.0
 */
@Dao
interface NearbyWorkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(nearbyWorker: NearbyWorkerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(nearbyWorkers: List<NearbyWorkerEntity>)

    @Query("SELECT * FROM NearbyWorkerEntity WHERE jobOfferId = :jobOfferId")
    fun observeByJobOffer(jobOfferId: Long): Flow<List<NearbyWorkerEntity>>

    @Query("SELECT * FROM NearbyWorkerEntity WHERE jobOfferId = :jobOfferId")
    suspend fun getAllByJobOffer(jobOfferId: Long): List<NearbyWorkerEntity>

    @Transaction
    @Query("DELETE FROM NearbyWorkerEntity WHERE jobOfferId = :jobOfferId")
    suspend fun clearByJobOffer(jobOfferId: Long)

    @Transaction
    suspend fun replaceAllByJobOffer(jobOfferId: Long, items: List<NearbyWorkerEntity>) {
        Log.d("NearbyWorkerDao", "replaceAllByJobOffer: $items")
        clearByJobOffer(jobOfferId)
        saveAll(items)
    }
}