package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel
import com.example.neuxum_cliente.ui.components.CitiesComponent
import com.example.neuxum_cliente.ui.components.PagerNavigationComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 19/08/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpCityScreen(
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
            "¿Cuál es tu ciudad?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Start
        )
        Text(
            "Selecciona la ciudad en la que quieres solicitar nuestros servicios.",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF858191),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(35.dp))
        CitiesComponent(
            initialSelectedCity = state.city,
            cities = listOf(
                "Bogotá",
                "Barranquilla",
                "Chía",
                "Medellín",
                "Cartagena",
                "Cúcuta",
                "Bucaramanga",
                "Cali"
            ),
            onCitySelected = {
                viewModel.onEvent(SignUpEvent.CityChanged(it))
            },
            maxHeight = 400.dp
        )
        Spacer(modifier = Modifier.weight(1f))
        PagerNavigationComponent(
            onBack = {
                go(AuthRoutes.SignUpBirthdayScreen)
            },
            onNext = {
                go(AuthRoutes.SignUpIDScreen)
            },
            enableNextButton = viewModel.signUpCityValidationPassed
        )
    }
}