package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.components.MyTextFieldComponent
import com.example.neuxum_cliente.ui.components.PagerNavigationComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpUserDataScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

    val textValue = rememberSaveable { mutableStateOf("") }
    Log.d("TAG", "SignUpUserDataScreen: ${state.email}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                top = 80.dp,    // ↑ increase this value
                end = 20.dp,
                bottom = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        )
        {
            Text(
                "¿Cómo te llamas?",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
            Text("Dinos cómo debemos llamarte \uD83D\uDE0A", fontSize = 14.sp)

            Text(
                "Nombre",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            MyTextFieldComponent(
                labelValue = "Ingrese su nombre",
                textValue = state.name,
                onTextSelected = {
                    viewModel.onEvent(SignUpEvent.NameChanged(it))
                },
                errorStatus = state.nameError
            )
            Text(
                "Apellido",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            MyTextFieldComponent(
                labelValue = "Ingrese su apellido",
                textValue = state.lastName,
                onTextSelected = {
                    viewModel.onEvent(SignUpEvent.LastNameChanged(it))
                },
                errorStatus = state.lastNameError
            )
            Spacer(modifier = Modifier.weight(1f))
            PagerNavigationComponent(
                onBack = { go(AuthRoutes.SignUpScreen) },
                onNext = { go(AuthRoutes.SignUpCellphoneScreen) },
                enableNextButton = viewModel.signUpUserDataValidationPassed
            )
        }
    }

}
