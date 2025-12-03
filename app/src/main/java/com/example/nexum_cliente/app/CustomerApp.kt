package com.example.nexum_cliente.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nexum_cliente.common.UserAuthState
import com.example.nexum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.nexum_cliente.ui.navigation.graphs.Graph
import com.example.nexum_cliente.ui.navigation.graphs.authGraph
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.home.HomeScreen
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_cliente.ui.presenter.splash.SplashEvent
import com.example.nexum_cliente.ui.presenter.splash.SplashScreen

@Composable
fun CustomerApp(
    authViewModel: AuthViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel()
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
        authGraph(navController, signUpViewModel)
        composable<AuthRoutes.HomeScreen>() { HomeScreen() }
        composable<AuthRoutes.SplashScreen>() { SplashScreen(navController = navController) }
    }
}