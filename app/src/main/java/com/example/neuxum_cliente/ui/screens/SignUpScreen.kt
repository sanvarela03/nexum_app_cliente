package com.example.neuxum_cliente.ui.screens

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
import com.example.neuxum_cliente.ui.componets.PagerNavigationComponent
import com.example.neuxum_cliente.ui.navigation.rutes.Routes
import com.example.neuxum_cliente.ui.theme.Neuxum_clienteTheme
import com.example.protapptest.ui.components.MyTextFieldComponent

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpScreen(
    go: (Any) -> Unit = {}
) {
    val textValue = rememberSaveable { mutableStateOf("") }
    Neuxum_clienteTheme {
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
                    onTextSelected = {},
                    errorStatus = true
                )
                Text(
                    "Apellido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                MyTextFieldComponent(
                    labelValue = "Ingrese su apellido",
                    onTextSelected = {},
                    errorStatus = true
                )
                Spacer(modifier = Modifier.weight(1f))
                PagerNavigationComponent(
                    onBack = { go (Routes.SignInScreen) },
                    onNext = { go (Routes.SignUpCellphoneScreen) }
                )
            }
        }
    }

}
