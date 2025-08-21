package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.componets.MyNumberFieldComponent
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpEvent
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpViewModel

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 24/07/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpCellphoneScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    go: (Any) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val state = viewModel.state
    val countryCodes = listOf(
        "🇨🇴 +57",
        "🇲🇽 +52",
        "🇨🇱 +56"
    )
    var currentSelectedCode by remember { mutableStateOf(state.phoneCode) }

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
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .border(
                                1.dp,
                                if (!state.phoneCodeError) Color.Red else Color(0xFFE6E6E6),
                                RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = if (currentSelectedCode.isEmpty()) "Código" else currentSelectedCode,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "More options"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                countryCodes.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            currentSelectedCode = it
                                            viewModel.onEvent(SignUpEvent.PhoneCodeChanged(currentSelectedCode))
                                            expanded = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    MyNumberFieldComponent(
                        modifier = Modifier
                            .width(150.dp)
                            .height(56.dp),
                        labelValue = "No. celular",
                        numberValue = state.phone,
                        onTextSelected = {
                            viewModel.onEvent(SignUpEvent.CellphoneChanged(it))
                        },
                        errorStatus = state.phoneError,
                        focusHops = 2
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PagerNavigationComponent(
                onBack = {
                    go(AuthRoutes.SignUpUserDataScreen)
                         },
                onNext = {
                    go(AuthRoutes.SignUpBirthdayScreen)
                },
                enableNextButton = viewModel.signUpPhoneDataValidationPassed
            )
        }
    }

}
