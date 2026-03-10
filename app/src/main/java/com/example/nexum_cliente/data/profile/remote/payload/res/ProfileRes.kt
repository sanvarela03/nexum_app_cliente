package com.example.nexum_cliente.data.profile.remote.payload.res


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
data class ProfileRes(
    val id: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phone: String,
    val imgUrl: String,
    val dateJoined: String,
    val lastLogin: String,
    val isEnabled: String,
)
