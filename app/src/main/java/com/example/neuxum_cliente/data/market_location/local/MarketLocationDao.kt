package com.example.neuxum_cliente.data.market_location.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.neuxum_cliente.data.category.local.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 04/09/2025
 * @version 1.0
 */
@Dao
interface MarketLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(locations: List<MarketLocationEntity>)

    @Query("SELECT * FROM MarketLocationEntity")
    fun observeAllLocations(): Flow<List<MarketLocationEntity>>

    @Query("SELECT * FROM MarketLocationEntity")
    suspend fun getAllLocations(): List<MarketLocationEntity?>

    @Transaction
    @Query("DELETE FROM MarketLocationEntity")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceLocations(locations: List<MarketLocationEntity>) {
        clearAll()
        saveAll(locations)
    }

}