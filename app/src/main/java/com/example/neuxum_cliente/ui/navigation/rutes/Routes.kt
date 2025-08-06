package com.example.neuxum_cliente.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
sealed class Routes {
    @Serializable
    object SignInScreen : Routes()

    @Serializable
    object SignUpScreen : Routes()

    @Serializable
    object SignUpCellphoneScreen : Routes()

    @Serializable
    object SignUpBirthdayScreen : Routes()
}