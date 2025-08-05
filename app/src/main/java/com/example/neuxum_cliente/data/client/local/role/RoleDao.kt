package com.example.neuxum_cliente.data.client.local.role

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRole(role: RoleEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoles(roles: List<RoleEntity>)

    @Query("DELETE FROM roleentity WHERE clientId = :clientId")
    suspend fun deleteRolesByClientId(clientId: Long)

    @Query("SELECT * FROM roleentity WHERE clientId = :clientId")
    suspend fun getRolesByClientId(clientId: Long): List<RoleEntity>
}