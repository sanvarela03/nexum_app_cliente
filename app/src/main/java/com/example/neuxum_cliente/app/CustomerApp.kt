package com.example.neuxum_cliente.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neuxum_cliente.common.UserAuthState
import com.example.neuxum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.neuxum_cliente.ui.navigation.graphs.Graph
import com.example.neuxum_cliente.ui.navigation.graphs.authGraph
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.home.HomeScreen
import com.example.neuxum_cliente.ui.presenter.splash.SplashEvent
import com.example.neuxum_cliente.ui.presenter.splash.SplashScreen

@Composable
fun CustomerApp(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val userState = authViewModel.isAuthenticated.collectAsState().value

    LaunchedEffect(userState) {
        authViewModel.onEvent(SplashEvent.CheckAuthentication)
    }

    NavHost(
        navController = navController,
        startDestination =
        when (userState) {
            UserAuthState.UNKNOWN -> {
                AuthRoutes.SplashScreen
            }

            UserAuthState.UNAUTHENTICATED -> {
                Graph.InitialGraph
            }

            UserAuthState.AUTHENTICATED -> {
                AuthRoutes.HomeScreen
            }
        }

    ) {
        authGraph(navController)
        composable<AuthRoutes.HomeScreen>() { HomeScreen() }
        composable<AuthRoutes.SplashScreen>() { SplashScreen(navController = navController) }
    }
}