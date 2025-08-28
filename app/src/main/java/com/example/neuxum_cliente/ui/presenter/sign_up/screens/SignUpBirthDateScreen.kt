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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.components.DateOfBirthPicker
import com.example.neuxum_cliente.ui.components.PagerNavigationComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel
import java.util.Calendar

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 24/07/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpBirthDateScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    go: (Any) -> Unit = {}
) {
    var year = viewModel.state.birthDateYear
    var month = viewModel.state.birthDateMonth
    var day = viewModel.state.birthDateDay

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
    ) {
        Text(
            "¿Cuál es tu fecha de nacimiento?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(35.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        )
        {
            Text(
                "Fecha de nacimiento",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(8.dp))

            DateOfBirthPicker(
                initialYear = year,
                initialMonth = month,
                initialDay = day,
                minYear = 1925,
                maxYear = Calendar.getInstance().get(Calendar.YEAR) - 18,
                onDayChanged = {
                    day = it
                    viewModel.onEvent(SignUpEvent.BirthDateDayChanged(day))
                },
                onMonthChanged = {
                    month = it
                    viewModel.onEvent(SignUpEvent.BirthDateMonthChanged(month))
                },
                onYearChanged = {
                    year = it
                    viewModel.onEvent(SignUpEvent.BirthDateYearChanged(year))
                }
            )

            Text("Seleccionado: %04d-%02d-%02d".format(year, month, day))
        }

        Spacer(modifier = Modifier.weight(1f))
        PagerNavigationComponent(
            onBack = {
                //TODO Navegar al registro
                go(AuthRoutes.SignUpCellphoneScreen)
            },
            onNext = {
                go(AuthRoutes.SignUpCityScreen)
            },
            enableNextButton = viewModel.signUpBirthDateValidationPassed
        )
    }
}