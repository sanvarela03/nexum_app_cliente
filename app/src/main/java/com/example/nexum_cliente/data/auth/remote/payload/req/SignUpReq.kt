package com.example.nexum_cliente.data.auth.remote.payload.req

import com.google.gson.annotations.SerializedName

data class SignUpReq(
    val username: String,
    val email: String,
    private val roles: List<String> = listOf("ROLE_USER", "ROLE_CLIENT"),
    @SerializedName("phone_code")
    val phoneCode: String = "NaN",
    @SerializedName("phone_number")
    val phoneNumber: String = "NaN",
    val password: String,
    @SerializedName("market_location_id")
    val marketLocationId: Long,
    @SerializedName("firebase_token")
    val firebaseToken: String = "NaN",
    val profile: ProfileReq,
)

