package com.example.nexum_cliente.app

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomerApp(
    userAuthState: UserAuthState,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    Log.d("CustomerApp", "userAuthState: $userAuthState")
    LaunchedEffect(userAuthState) {
        Log.d("CustomerApp", "userAuthState: $userAuthState")
        snapshotFlow { userAuthState }
            .drop(1) // Ignore the initial null value
            .filter { it == UserAuthState.UNAUTHENTICATED }
            .collect {
                navController.navigate(Graph.InitialGraph) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = when (userAuthState) {
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

        protectedComposable<AuthRoutes.HomeScreen>(
            isAuthenticated = userAuthState == UserAuthState.AUTHENTICATED,
            onUnauthorized = {
                navController.navigate(Graph.InitialGraph) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        ) {
            HomeScreen()
        }
//        composable<AuthRoutes.HomeScreen>() { HomeScreen() }
        composable<AuthRoutes.SplashScreen>() {
            SplashScreen(
                navController = navController,
                userAuthState = userAuthState
            )
        }
    }
}
