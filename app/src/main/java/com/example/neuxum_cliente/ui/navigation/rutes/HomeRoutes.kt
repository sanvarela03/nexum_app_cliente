package com.example.neuxum_cliente.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
sealed class HomeRoutes {
    @Serializable
    object RequestsScreen : HomeRoutes()

    @Serializable
    object CategoriesScreen : HomeRoutes()

    @Serializable
    object WalletScreen : HomeRoutes()

    @Serializable
    object NotificationsScreen : HomeRoutes()


}