package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import android.graphics.drawable.Icon
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.components.ImagePreview
import com.example.neuxum_cliente.ui.components.TosAndPolicyText
import com.example.neuxum_cliente.ui.components.UploadImageButtonComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.neuxum_cliente.ui.components.PagerNavigationComponent
import com.example.neuxum_cliente.ui.components.dashedBorder
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.neuxum_cliente.R

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpUploadDocumentScreen (
    viewModel: SignUpViewModel = hiltViewModel(),
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
