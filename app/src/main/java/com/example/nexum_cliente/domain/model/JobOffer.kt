package com.example.nexum_cliente.domain.model


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 4/3/2026
 * @version 1.0
 */
data class JobOffer(
    val id: Long = 0,
    val uuid: String = "NAN",
    val clientId: Long = 0,
    val categoryId: Long = 0,
    val categoryName: String = "NAN",
    val title: String = "NAN",
    val description: String = "NAN",
    val requestedDate: String = "00/00/0000",
    val h3Idx: String = "NAN",
    val createdAt: String = "00/00/0000",
    val location: Location = Location(),
    val photos: List<String> = emptyList(),
    val status: String = "SEARCHING"
)