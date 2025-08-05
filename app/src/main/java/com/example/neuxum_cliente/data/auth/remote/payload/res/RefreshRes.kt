package com.example.neuxum_cliente.data.auth.remote.payload.res

data class RefreshRes(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)