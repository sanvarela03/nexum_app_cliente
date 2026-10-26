package com.example.nexum_cliente.data.job_application.remote.payload.res

import com.google.gson.annotations.SerializedName

data class MaterialRes(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName(value = "unit_price", alternate = ["unitPrice"])
    val unitPrice: Double? = null,
    @SerializedName(value = "created_at", alternate = ["createdAt"])
    val createdAt: String? = null
)
