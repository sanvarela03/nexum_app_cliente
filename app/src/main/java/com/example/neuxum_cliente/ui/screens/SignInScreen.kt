package com.example.neuxum_cliente.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuxum_cliente.ui.theme.Neuxum_clienteTheme
import com.example.protapptest.ui.components.ButtonComponent
import com.example.protapptest.ui.components.MyTextFieldComponent

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignInScreen() {
    Neuxum_clienteTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                Text(
                    "Ingresar a tu cuenta",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                Text("Ingrese su correo para iniciar sesión", fontSize = 18.sp)

                MyTextFieldComponent(
                    labelValue = "Ingrese su correo",
                    icon = Icons.Default.Email,
                    onTextSelected = {},
                    errorStatus = true
                )
                MyTextFieldComponent(
                    labelValue = "Ingrese su contraseña",
                    icon = Icons.Default.Lock,
                    onTextSelected = {},
                    errorStatus = true
                )
                Text("¿Olvidaste tu constraseña?", fontSize = 14.sp, textAlign = TextAlign.Right)
                ButtonComponent(
                    value = "Ingresar",
                    isEnabled = true,
                ) {

                }
            }
        }
    }

}
