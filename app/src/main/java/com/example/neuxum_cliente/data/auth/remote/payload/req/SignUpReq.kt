package com.example.neuxum_cliente.data.auth.remote.payload.req

data class SignUpReq(
    val username: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String = "NaN",
    val firebaseToken: String,
    val password: String,
    private val role: List<String> = listOf("ROLE_USER", "ROLE_CLIENT")
)