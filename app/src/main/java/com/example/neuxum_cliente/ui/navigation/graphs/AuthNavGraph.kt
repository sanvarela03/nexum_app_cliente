package com.example.neuxum_cliente.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_in.SignInScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    navigation<Graph.InitialGraph>(startDestination = AuthRoutes.SignInScreen) {
        composable<AuthRoutes.SignInScreen> { SignInScreen(go = { navController.navigate(it) }) }
        composable<AuthRoutes.SignUpScreen> { SignUpScreen() }
    }
}