package com.example.neuxum_cliente.ui.presenter.splash

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
import com.example.neuxum_cliente.R
import com.example.neuxum_cliente.common.UserAuthState
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    userAuthState: UserAuthState? = null
) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.popBackStack()
//        navController.navigate(
//            if (userAuthState == UserAuthState.AUTHENTICATED)
//                Screen.HomeScreen.route
//            else
//                Screen.SignInScreen.route
//        )
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


@Composable
@Preview(showBackground = true)
fun SplashScreenPreview() {
    Splash()
}