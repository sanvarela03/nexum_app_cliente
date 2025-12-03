package com.example.nexum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_cliente.ui.components.PagerNavigationComponent
import com.example.nexum_cliente.ui.components.PasswordTextFieldComponent
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 30/08/2025
 * @version 1.0
 */
@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun SignUpPasswordScreen (
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

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
            "Elige una contraseña segura",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Start
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                "Contraseña",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            PasswordTextFieldComponent(
                labelValue = "Ingrese su contraseña",
                password = state.password,
                icon = Icons.Default.Lock,
                onTextSelected = {
                    viewModel.onEvent(SignUpEvent.PasswordChanged(it))
                },
                errorStatus = state.passwordError
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                "Confirmar contraseña",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            PasswordTextFieldComponent(
                labelValue = "Ingrese su contraseña",
                password = state.confirmPassword,
                icon = Icons.Default.Lock,
                onTextSelected = {
                    viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(it))
                },
                errorStatus = state.confirmPasswordError
            )
        }
        PagerNavigationComponent(
            onBack = {
                go(AuthRoutes.SignUpUploadDocumentScreen)
            },
            onNext = {
                go(AuthRoutes.SignUpProfilePictureScreen)
            },
            enableNextButton = viewModel.signUpPasswordValidationPassed
        )
    }
}
