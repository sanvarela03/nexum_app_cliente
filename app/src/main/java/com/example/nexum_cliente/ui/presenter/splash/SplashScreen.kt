package com.example.nexum_cliente.ui.presenter.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nexum_cliente.R
import com.example.nexum_cliente.common.UserAuthState
import com.example.nexum_cliente.ui.navigation.graphs.Graph
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes

@Composable
fun SplashScreen(
    navController: NavController,
    userAuthState: UserAuthState?
) {
    Log.d("SplashScreen", "userAuthState: $userAuthState")
    LaunchedEffect(userAuthState) {
        when (userAuthState) {
            UserAuthState.AUTHENTICATED -> {
                navController.navigate(AuthRoutes.HomeScreen) {
                    popUpTo(AuthRoutes.SplashScreen) { inclusive = true }
                }
            }
            UserAuthState.UNAUTHENTICATED -> {
                navController.navigate(Graph.Auth) {
                    popUpTo(AuthRoutes.SplashScreen) { inclusive = true }
                }
            }
            UserAuthState.UNKNOWN, null -> {

            }
        }
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(R.drawable.nexum_logo_2),
            contentDescription = "Logo app",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(120.dp)
        )
    }
}

//
@Composable
@Preview(showBackground = true)
fun SplashScreenPreview() {
    Splash()
}
