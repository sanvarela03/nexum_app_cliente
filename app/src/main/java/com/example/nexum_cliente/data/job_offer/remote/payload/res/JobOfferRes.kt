package com.example.nexum_cliente.data.job_offer.remote.payload.res


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/31/2026
 * @version 1.0
 */
data class JobOfferRes(
    val jobOfferId: Long,
    val jobOfferUuid: String,
    val clientId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val description: String,
    val requiredDate: String,
    val h3Idx: String,
    val createdAt: String,
    val location: Location,
    val address: String? = null,
    val imageUrls: List<String>,
    val status: String? = null,
)

data class Location(
    val lat: Double,
    val lng: Double
)