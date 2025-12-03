package com.example.nexum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.nexum_cliente.ui.components.MyNumberFieldComponent
import com.example.nexum_cliente.ui.components.PagerNavigationComponent
import com.example.nexum_cliente.ui.components.TosAndPolicyText
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun SignUpIDScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 10.dp,
                top = 80.dp,
                end = 10.dp,
                bottom = 20.dp
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "¿Cuál es tu n° de documento de identidad?",
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
        Text(
            "Número de documento",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        MyNumberFieldComponent(
            modifier = Modifier
                .fillMaxWidth(),
            labelValue = "Ingrese su n° de documento",
            numberValue = state.documentNumber,
            onTextSelected = {
                viewModel.onEvent(SignUpEvent.DocumentNumberChanged(it))
            },
            errorStatus = state.documentNumberError,
            focusHops = 2
        )
        Spacer(modifier = Modifier.weight(1f))
        PagerNavigationComponent(
            onBack = {
                go(AuthRoutes.SignUpCityScreen)
            },
            onNext = {
                go(AuthRoutes.SignUpUploadDocumentScreen)
            },
            enableNextButton = viewModel.signUpDocumentNumberValidationPassed
        )
    }
}
