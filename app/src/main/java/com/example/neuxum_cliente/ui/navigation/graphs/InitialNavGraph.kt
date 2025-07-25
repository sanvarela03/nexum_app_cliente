package com.example.neuxum_cliente.ui.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.neuxum_cliente.ui.navigation.rutes.Routes
import com.example.neuxum_cliente.ui.screens.SignInScreen
import com.example.neuxum_cliente.ui.screens.SignUpScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
fun NavGraphBuilder.initialGraph(go: (Any) -> Unit) {
    navigation<Graph.InitialGraph>(startDestination = Routes.SignInScreen) {
        composable<Routes.SignInScreen> { SignInScreen(go = go) }
        composable<Routes.SignUpScreen> { SignUpScreen() }
    }
}