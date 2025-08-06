package com.example.neuxum_cliente.data.category.local

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
 * @since 8/5/2025
 * @version 1.0
 */
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(categories: List<CategoryEntity>)

    @Query("SELECT * FROM Categoryentity")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM Categoryentity")
    suspend fun getAllCategories(): List<CategoryEntity?>

    @Transaction
    @Query("DELETE FROM Categoryentity")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceCategories(categories: List<CategoryEntity>) {
        clearAll()
        saveAll(categories)
    }

}