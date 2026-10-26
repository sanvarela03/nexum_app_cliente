package com.example.nexum_cliente.data.review.remote.payload.res

import com.google.gson.annotations.SerializedName

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class ReviewRes(
    @SerializedName("id")
    val id: Long = 0L,
    @SerializedName(value = "contract_id", alternate = ["contractId"])
    val contractId: Long = 0L,
    @SerializedName(value = "reviewer_id", alternate = ["reviewerId"])
    val reviewerId: Long = 0L,
    @SerializedName(value = "reviewer_username", alternate = ["reviewerUsername"])
    val reviewerUsername: String? = null,
    @SerializedName(value = "reviewee_id", alternate = ["revieweeId"])
    val revieweeId: Long = 0L,
    @SerializedName(value = "reviewee_username", alternate = ["revieweeUsername"])
    val revieweeUsername: String? = null,
    @SerializedName("rating")
    val rating: Int = 5,
    @SerializedName("comment")
    val comment: String? = null,
    @SerializedName(value = "created_at", alternate = ["createdAt"])
    val createdAt: String? = null
)
