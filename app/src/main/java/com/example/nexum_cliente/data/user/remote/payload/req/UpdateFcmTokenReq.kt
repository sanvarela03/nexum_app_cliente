package com.example.nexum_cliente.data.user.remote.payload.req

import com.google.gson.annotations.SerializedName

data class UpdateFcmTokenReq(
    @SerializedName("fcm_token")
    val fcmToken: String
)
