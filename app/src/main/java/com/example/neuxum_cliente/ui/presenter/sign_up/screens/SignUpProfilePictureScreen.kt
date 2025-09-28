package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.components.ButtonComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.constraintlayout.compose.MotionScene
import com.example.neuxum_cliente.R
import com.example.neuxum_cliente.ui.components.ImagePreview
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/09/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpProfilePictureScreen (
    viewModel: SignUpViewModel = hiltViewModel(),
    go: (Any) -> Unit = {}
) {

    val state = viewModel.state

    val profilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(SignUpEvent.ProfilePictureUriChanged(it))
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                top = 60.dp,
                end = 20.dp,
                bottom = 40.dp
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Perfil",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    tint = Color.Black,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .semantics { contentDescription = "Profile image with camera button" },
                contentAlignment = Alignment.Center
            ) {
                if (state.profilePictureUri != Uri.EMPTY) {
                    ImagePreview(
                        uri = state.profilePictureUrl,
                        onRemoveImage = {
                            viewModel.onEvent(SignUpEvent.ProfilePictureUriChanged(Uri.EMPTY))
                        },
                        index = 0,
                        contentScale = ContentScale.FillWidth
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFBDBDBD)), // gray placeholder
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            modifier = Modifier.size(80.dp),
                            contentDescription = "Default avatar",
                            tint = Color.White
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = Color.Gray,
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clickable {
                            profilePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Outlined.AddAPhoto,
                            contentDescription = "Change photo",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Row {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email icon",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp)) // space between icon and text
                Text(text = state.email)
            }
            Row {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone icon",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp)) // space between icon and text
                Text(text = "${state.phoneCode} ${state.phone}")
            }
            Row {
                Icon(
                    painter = painterResource(R.drawable.vector),
                    contentDescription = "ID icon",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp)) // space between icon and text
                Text(text = state.documentNumber)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Location icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            state.city?.let { loc ->
                Text(text = "${loc.city}, ${loc.state}, ${loc.country}")
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        ButtonComponent(
            value = "Terminar registro",
            isEnabled = viewModel.signUpProfilePictureValidationPassed,
        ) {
            go(AuthRoutes.SignUpUserDataScreen)
        }
    }

}