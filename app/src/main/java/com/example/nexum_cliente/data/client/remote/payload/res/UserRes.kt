package com.example.nexum_cliente.data.client.remote.payload.res

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Serializable
data class UserRes(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val verificationCode: String,
    val verificationCodeTimestamp: String,
    val firebaseToken: String,
    val phone: String,
    val isEnabled: Boolean,
    val dateJoined: String,
    val lastLogin: String,
    val imgUrl: String,
    val roles: List<String>
)