package com.example.nexum_cliente.data.client.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.nexum_cliente.data.local_storage.BaseDao
import kotlinx.coroutines.flow.Flow


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Dao
interface ClientDao : BaseDao<ClientEntity> {
    @Query("SELECT * FROM ClientEntity")
    fun observe(): Flow<List<ClientEntity>>

    @Query("DELETE FROM ClientEntity ")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(entities: List<ClientEntity>) {
        clearAll()
        insertAll(entities)
    }

    @Query("SELECT * FROM ClientEntity")
    suspend fun getAll(): List<ClientEntity>

    @Query("SELECT * FROM ClientEntity WHERE clientId = :id")
    suspend fun getById(id: Long): ClientEntity?

    @Transaction
    @Query("SELECT * FROM ClientEntity WHERE clientId = :clientId")
    suspend fun getClientWithRoles(clientId: Long): List<ClientWithRoles>
}