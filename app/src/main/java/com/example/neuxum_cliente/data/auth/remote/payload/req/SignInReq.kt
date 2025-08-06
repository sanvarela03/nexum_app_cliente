package com.example.neuxum_cliente.data.auth.remote.payload.req

data class SignInReq(
//    @SerializedName("")
    val username: String,
    val password: String,
    val firebaseToken: String
)
