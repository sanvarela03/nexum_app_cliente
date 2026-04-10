package com.example.nexum_cliente.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nexum_cliente.common.UserAuthState
import com.example.nexum_cliente.common.protectedComposable
import com.example.nexum_cliente.ui.navigation.graphs.Graph
import com.example.nexum_cliente.ui.navigation.graphs.authGraph
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.home.HomeScreen
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_cliente.ui.presenter.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomerApp(
    userAuthState: UserAuthState,
    startDestination: AuthRoutes,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = when (userAuthState) {
            UserAuthState.UNKNOWN -> AuthRoutes.SplashScreen
            UserAuthState.UNAUTHENTICATED -> Graph.Auth
            UserAuthState.AUTHENTICATED -> AuthRoutes.HomeScreen
        },
        modifier = Modifier.fillMaxSize()
    ) {
        authGraph(navController, signUpViewModel, startDestination)

        protectedComposable<AuthRoutes.HomeScreen>(
            isAuthenticated = userAuthState == UserAuthState.AUTHENTICATED,
            onUnauthorized = {
                navController.navigate(Graph.Auth) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        ) {
            HomeScreen()
        }
        composable<AuthRoutes.SplashScreen> {
            SplashScreen(
                navController = navController,
                userAuthState = userAuthState
            )
        }
    }
}