package com.example.neuxum_cliente.data.client.local

import androidx.room.Embedded
import androidx.room.Relation
import com.example.neuxum_cliente.data.client.local.role.RoleEntity


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
data class ClientWithRoles (
    @Embedded val client : ClientEntity,

    @Relation(
        parentColumn = "clientId",
        entityColumn = "clientId"
    )
    val roles : List<RoleEntity>
)