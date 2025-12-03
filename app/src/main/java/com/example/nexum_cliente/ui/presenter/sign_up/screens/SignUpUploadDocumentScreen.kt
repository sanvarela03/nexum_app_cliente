package com.example.nexum_cliente.ui.presenter.sign_up.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_cliente.ui.components.ImagePreview
import com.example.nexum_cliente.ui.components.TosAndPolicyText
import com.example.nexum_cliente.ui.components.UploadImageButtonComponent
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.nexum_cliente.ui.components.PagerNavigationComponent
import com.example.nexum_cliente.ui.components.dashedBorder
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_cliente.R

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun SignUpUploadDocumentScreen (
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(SignUpEvent.FrontDocumentUriChanged(it))
            }
        }
    )

    val backPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(SignUpEvent.BackDocumentUriChanged(it))
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 10.dp,
                top = 80.dp,    // ↑ increase this value
                end = 10.dp,
                bottom = 20.dp
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Subir tus documentos",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(10.dp))
        TosAndPolicyText(
            initTxt = "Solicitamos esta información para asegurar nuestras operaciones, consulta nuestros ",
            signUpTxt = "términos de uso",
            policyTxt = "política de privacidad.",
            go,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
                color = Color(0xFF000000)
            ),
            defaultTextStyle = SpanStyle(
                color = Color(0xFF858191),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            ),
            defaultLinkStyle = SpanStyle(
                color = Color(0xFF858191),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        UploadImageButtonComponent(
            text = "Frente del documento",
            leadingIcon = painterResource(R.drawable.vector),
            leadingIconTint = Color.Black, // 👈 add this param inside your component
            trailingIcon = Icons.AutoMirrored.Filled.ArrowForwardIos,
        ) {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        UploadImageButtonComponent(
            text = "Reverso del documento",
            leadingIcon = painterResource(R.drawable.vector_back),
            leadingIconTint = Color.Black, // 👈
            trailingIcon = Icons.AutoMirrored.Filled.ArrowForwardIos,
        ) {
            backPhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .padding(5.dp)
                    .dashedBorder(
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp),
                        strokeWidth = 3.dp,
                        dashLength = 10.dp,
                        gapLength = 10.dp,
                    ),
                contentAlignment = Alignment.Center
            ) {
                ImagePreview(
                    uri = state.frontDocumentUrl,
                    onRemoveImage = {
                        viewModel.onEvent(SignUpEvent.FrontDocumentUriChanged(Uri.EMPTY))
                    },
                    index = 0,
                    contentScale = ContentScale.FillWidth
                )
            }
            VerticalDivider(
                color = Color.Gray,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(horizontal = 8.dp) // margin left and right
                    .height(120.dp)
            )
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .padding(5.dp)
                    .dashedBorder(
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp),
                        strokeWidth = 3.dp,
                        dashLength = 10.dp,
                        gapLength = 10.dp,
                    ),
                contentAlignment = Alignment.Center
            ) {
                ImagePreview(
                    uri = state.backDocumentUrl,
                    onRemoveImage = {
                        viewModel.onEvent(SignUpEvent.BackDocumentUriChanged(Uri.EMPTY)) },
                    index = 0,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        PagerNavigationComponent(
            onBack = {
                go(AuthRoutes.SignUpIDScreen)
            },
            onNext = {
                go(AuthRoutes.SignUpPasswordScreen)
            },
            enableNextButton = viewModel.signUpDocumentImagesValidationPassed
        )


    }
}
