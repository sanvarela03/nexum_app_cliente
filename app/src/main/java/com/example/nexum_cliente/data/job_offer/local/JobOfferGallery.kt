package com.example.nexum_cliente.data.job_offer.local

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 4/3/2026
 * @version 1.0
 */
@Entity(tableName = "job_offer_gallery")
data class JobOfferGallery(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val jobOfferId: Long,
    val imageUrl: String,
)