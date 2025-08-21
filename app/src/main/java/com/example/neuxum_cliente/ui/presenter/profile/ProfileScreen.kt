package com.example.neuxum_cliente.ui.presenter.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.neuxum_cliente.data.client.local.ClientEntity
import com.example.neuxum_cliente.ui.presenter.home.HomeViewModel


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state = homeViewModel.state
    val client: ClientEntity? = state.clientEntity
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = client?.imgUrl,
                contentDescription = "Profile Image",
                modifier = Modifier.size(128.dp)
            )
            Text("Profile Screen")
            Text("Name: ${client?.firstName} ${client?.lastName}")
            Text("Email: ${client?.email}")
            Text("Phone: ${client?.phone}")
            Text("Username: ${client?.username}")
            Text("Date Joined: ${client?.dateJoined}")
            Text("Last Login: ${client?.lastLogin}")
            Text("Enabled: ${client?.isEnabled}")
            Text("Verification Code: ${client?.verificationCode}")
            Text("Verification Code Timestamp: ${client?.verificationCodeTimestamp}")
            Text("Firebase Token: ${client?.firebaseToken}")
        }


    }
}