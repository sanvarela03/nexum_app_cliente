package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.neuxum_cliente.ui.componets.MyNumberFieldComponent
import com.example.neuxum_cliente.ui.componets.PagerNavigationComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpIDScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                top = 80.dp,    // ↑ increase this value
                end = 20.dp,
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
        TosAndPolicyText {  }
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
                go(AuthRoutes.SignUpBirthdayScreen)
            },
            onNext = {},
            enableNextButton = viewModel.signUpDocumentNumberValidationPassed

        )
    }
}

@Composable
private fun TosAndPolicyText(go: (Any) -> Unit) {
    val initTxt = "Solicitamos esta información para asegurar nuestras operaciones, consulta nuestros "
    val signUpTxt = "términos de uso"
    val policyTxt = "política de privacidad."

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
            textAlign = TextAlign.Start,
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