package com.example.neuxum_cliente.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.neuxum_cliente.ui.componets.PagerNavigation
import com.example.neuxum_cliente.ui.theme.Neuxum_clienteTheme
import com.example.protapptest.ui.components.MyTextFieldComponent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 24/07/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpCellphoneScreen() {
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
                    "¿Cuál es tu número de teléfono?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Start,
                )
                Text(
                    "Celular",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        MyTextFieldComponent(
                            modifier = Modifier
                                .width(150.dp)
                                .height(56.dp),
                            labelValue = "Código",
                            onTextSelected = {},
                            trailingIcon = Icons.Default.ArrowDropDown,
                            errorStatus = true,
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        MyTextFieldComponent(
                            modifier = Modifier
                                .width(150.dp)
                                .height(56.dp),
                            labelValue = "No. celular",
                            onTextSelected = {},
                            errorStatus = true
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                PagerNavigation(
                    onBack = { /* nothing yet */ },
                    onNext = { /* nothing yet */ }
                )
            }
        }
    }

}
