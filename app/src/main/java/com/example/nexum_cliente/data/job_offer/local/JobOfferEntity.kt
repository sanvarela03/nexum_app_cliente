package com.example.nexum_cliente.data.job_offer.local

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
@Entity(tableName = "job_offers")
data class JobOfferEntity(
    @PrimaryKey
    val id: Long,
    val uuid: String,
    val clientId: Long,
    val title: String,
    val description: String,
    val categoryId: Long,
    val categoryName: String,
    val h3Idx: String,
    val requestedDate: String,
    val createdAt: String,
    val lat: Double,
    val lng: Double,
)

