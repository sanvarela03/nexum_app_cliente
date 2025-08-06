package com.example.neuxum_cliente.data.auth.remote.payload.res

data class SignInRes(
    var id: Long,
    val username: String,
    val email: String,
    val roles: List<String>,
    val token: String,
    val type: String,
    val refreshToken: String
)
