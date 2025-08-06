package com.example.neuxum_cliente.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
 @Serializable
sealed class AuthRoutes {
    @Serializable
    object SignInScreen : AuthRoutes()

    @Serializable
    object SignUpScreen : AuthRoutes()

    @Serializable
    object SplashScreen : AuthRoutes()

    @Serializable
    object HomeScreen : AuthRoutes()

    @Serializable
    object SignUpBirthdayScreen : AuthRoutes()

    @Serializable
    object SignUpCellphoneScreen : AuthRoutes()

}