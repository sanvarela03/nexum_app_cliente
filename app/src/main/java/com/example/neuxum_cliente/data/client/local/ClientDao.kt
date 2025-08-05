package com.example.neuxum_cliente.data.client.local

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
 * @since 8/2/2025
 * @version 1.0
 */
@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(client: ClientEntity)

    @Query("DELETE FROM cliententity WHERE clientId = :clientId")
    suspend fun deleteClientById(clientId: Long)

    @Query("SELECT * FROM cliententity WHERE userId = :userId")
    suspend fun getClientByUserId(userId: Long): ClientEntity?

    @Query("SELECT * FROM cliententity WHERE userId = :userId")
    fun observeClientByUserId(userId: Long?): Flow<ClientEntity?>

    @Query("SELECT * FROM cliententity")
    suspend fun getAllClients(): List<ClientEntity>

    @Transaction
    @Query("DELETE FROM cliententity")
    suspend fun clearAll()

    @Transaction
    @Query("SELECT * FROM ClientEntity WHERE clientId = :clientId")
    suspend fun getClientWithRoles(clientId: Long): List<ClientWithRoles>

    @Transaction
    suspend fun replaceClient(client: ClientEntity) {
        clearAll()
        save(client)
    }
}