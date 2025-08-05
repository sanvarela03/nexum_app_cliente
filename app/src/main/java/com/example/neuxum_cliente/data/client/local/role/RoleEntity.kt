package com.example.neuxum_cliente.data.client.local.role

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Entity
data class RoleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var clientId: Long
)
