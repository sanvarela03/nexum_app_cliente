package com.example.neuxum_cliente.ui.presenter.sign_up.screens

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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.neuxum_cliente.R
import com.example.neuxum_cliente.ui.componets.ButtonComponent
import com.example.neuxum_cliente.ui.componets.DividerTextComponent
import com.example.neuxum_cliente.ui.componets.MyTextFieldComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 05/08/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
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
                icon = Icons.Default.Email,
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

            TosAndPolicyText(go)

            SignUpText(go)

        }
    }

}

@Composable
private fun TosAndPolicyText(go: (Any) -> Unit) {
    val initTxt = "Al hacer clic en continuar, acepta nuestros "
    val signUpTxt = "Términos de servicio"
    val policyTxt = "Política de privacidad."

    val annotatedString = buildAnnotatedString {
        append(initTxt)
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = signUpTxt, annotation = signUpTxt)
            append(signUpTxt)
        }
        append(" y ")
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = policyTxt, annotation = policyTxt)
            append(policyTxt)
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF000000)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset).firstOrNull()
                ?.also { span ->
                    if ((span.item == signUpTxt)) {
                        go(AuthRoutes.SignUpScreen)
                    }
                }
        }
    )
}

@Composable
private fun SignUpText(go: (Any) -> Unit) {
    val initTxt = "¿Ya tienes una cuenta?  "
    val signUpTxt = "Inicia sesión"

    val annotatedString = buildAnnotatedString {
        append(initTxt)
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = signUpTxt, annotation = signUpTxt)
            append(signUpTxt)
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF000000)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset).firstOrNull()
                ?.also { span ->
                    if ((span.item == signUpTxt)) {
                        //TODO Navegar al inicio de sesión
                        go(AuthRoutes.SignInScreen)
                    }
                }
        }
    )
}