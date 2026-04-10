package com.example.nexum_cliente.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@Serializable
sealed class JobOfferRoutes {
    @Serializable
    data class JobOfferScreen(
        val categoryId: Long,
    ) : JobOfferRoutes()

    @Serializable
    data class TrackingScreen(
        val jobOfferId: String
    ) : JobOfferRoutes()
}