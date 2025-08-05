package com.example.neuxum_cliente.data.client.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neuxum_cliente.data.client.local.ClientDao
import com.example.neuxum_cliente.data.client.local.ClientEntity
import com.example.neuxum_cliente.data.client.local.role.RoleDao
import com.example.neuxum_cliente.data.client.local.role.RoleEntity


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Database(
    entities = [
        ClientEntity::class,
        RoleEntity::class,
    ],
    version = 1
)
abstract class ClientDb : RoomDatabase() {
    abstract val clientDao: ClientDao
    abstract val roleDao: RoleDao

}