package com.example.nexum_cliente.data.auth.remote.payload.res

data class SignInRes(
    val id: Long,
    val username: String,
    val email: String,
    val token: String?,
    val refreshToken: String?,
    val type: String,
    val roles: List<String>
)
