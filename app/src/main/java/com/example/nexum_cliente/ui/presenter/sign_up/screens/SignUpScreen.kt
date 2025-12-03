package com.example.nexum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_cliente.R
import com.example.nexum_cliente.ui.components.ButtonComponent
import com.example.nexum_cliente.ui.components.DividerTextComponent
import com.example.nexum_cliente.ui.components.MyTextFieldComponent
import com.example.nexum_cliente.ui.components.RichClickableText
import com.example.nexum_cliente.ui.components.TosAndPolicyText
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel

/** 
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 05/08/2025
 * @version 1.0
 */
@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun SignUpScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {},
) {

    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            Image(
                painter = painterResource(R.drawable.nexum_logo_2),
                modifier = Modifier
                    .size(140.dp),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Crea tu cuenta",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
            Text("Ingrese su correo para registrarse", fontSize = 18.sp)

            MyTextFieldComponent(
                labelValue = "Ingrese su correo",
                textValue = state.email,
                leadingIcon = Icons.Default.Email,
                onTextSelected = {
                    viewModel.onEvent(SignUpEvent.EmailChanged(it))
                },
                errorStatus = state.emailError
            )

            ButtonComponent(
                value = "Continuar",
                isEnabled = viewModel.signUpEmailValidationPassed,
            ) {
                go(AuthRoutes.SignUpUserDataScreen)
            }
            DividerTextComponent()

            TosAndPolicyText(
                initTxt = "Al hacer clic en continuar, acepta nuestros  ",
                signUpTxt = "Términos de servicio",
                policyTxt = "Política de privacidad.",
                go
            )

            RichClickableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF000000)
                )
            ) {
                text("¿Ya tienes una cuenta?  ")
                link("Inicia sesión") { go(AuthRoutes.SignInScreen) }
            }

        }
    }

}
