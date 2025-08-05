package com.example.neuxum_cliente.data.client.local

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
data class ClientEntity(
    @PrimaryKey
    val clientId: Long?,
    val userId: Long?,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val verificationCode: String?,
    val verificationCodeTimestamp: String?,
    val firebaseToken: String?,
    val phone: String?,
    val isEnabled: Boolean,
    val dateJoined: String,
    val lastLogin: String,
    val imgUrl: String?,
)

