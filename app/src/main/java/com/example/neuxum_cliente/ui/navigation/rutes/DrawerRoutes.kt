package com.example.neuxum_cliente.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
sealed class DrawerRoutes {
    @Serializable
    object ProfileScreen : DrawerRoutes()

    @Serializable
    object SettingsScreen : DrawerRoutes()


}