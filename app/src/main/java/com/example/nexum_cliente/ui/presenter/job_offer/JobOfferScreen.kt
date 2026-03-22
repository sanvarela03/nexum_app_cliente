package com.example.nexum_cliente.ui.presenter.job_offer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_cliente.R
import com.example.nexum_cliente.ui.components.ButtonComponent
import com.example.nexum_cliente.ui.components.FilterChipComponent2
import com.example.nexum_cliente.ui.components.MultiplePhotoPickerComponent
import com.example.nexum_cliente.ui.components.MyDialog2
import com.example.nexum_cliente.ui.components.MyTextFieldComponent
import com.example.nexum_cliente.ui.components.MyTextFieldComponent2
import com.example.nexum_cliente.ui.components.Stepper
import com.example.nexum_cliente.ui.components.TimePickerDialog
import com.example.nexum_cliente.ui.presenter.map.MapScreen
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobOfferScreen(
    categoryId: Long,
    viewModel: JobOfferViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.onEvent(JobOfferEvent.CategoryIdChanged(categoryId))
    }
    val state = viewModel.state

    MapDialog(viewModel)

    if (state.showDatePickerDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            ),
            onDismissRequest = { viewModel.onEvent(JobOfferEvent.ShowDatePicker(false)) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onEvent(JobOfferEvent.DateSelected(it))
                    }
                    viewModel.onEvent(JobOfferEvent.ShowDatePicker(false))
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(JobOfferEvent.ShowDatePicker(false)) }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showTimePickerDialog) {
        val timePickerState = rememberTimePickerState()
        TimePickerDialog(
            onDismissRequest = { viewModel.onEvent(JobOfferEvent.ShowTimePicker(false)) },
            onConfirm = {
                viewModel.onEvent(
                    JobOfferEvent.TimeSelected(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                )
                viewModel.onEvent(JobOfferEvent.ShowTimePicker(false))
            }
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.White,
                    clockDialSelectedContentColor = Color.Black,
                    clockDialUnselectedContentColor = Color.Black,
                    selectorColor = Color.Gray,
                    containerColor = Color.White,
                    periodSelectorBorderColor = Color.Black,
                )
            )
        }
    }

    JobOfferScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onShowMapDialog = { viewModel.showMapDialog = true },
        onSubmit = { viewModel.onEvent(JobOfferEvent.Submit) },
        onDismissSuccess = { viewModel.onEvent(JobOfferEvent.DismissSuccessDialog) },
        onNavigateSuccess = {
            viewModel.onEvent(JobOfferEvent.DismissSuccessDialog)

        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobOfferScreenContent(
    state: JobOfferState,
    onEvent: (JobOfferEvent) -> Unit,
    onShowMapDialog: () -> Unit,
    onSubmit: () -> Unit,
    onDismissSuccess: () -> Unit = {},
    onNavigateSuccess: () -> Unit = {}
) {
    Nexum_clienteTheme() {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Stepper(
                numberOfSteps = 4,
                currentStep = 2,
                selectedColor = Color(0xFF009963),
                unSelectedColor = Color(0xFFE6E6E6)
            )
            Text("Agregar dirección", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            MyTextFieldComponent2(
                labelValue = "Dirección",
                textValue = state.address,
                leadingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Ubicación actual"
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = onShowMapDialog) {
                        Icon(
                            painter = painterResource(R.drawable.distance),
                            contentDescription = "Abrir mapa"
                        )
                    }
                },
                onTextSelected = {
                    onEvent(JobOfferEvent.AddressChanged(it))
                }
            )

            Text("Detalle de la solicitud", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            MyTextFieldComponent(
                labelValue = "Titulo",
                textValue = state.title,
                trailingIcon = Icons.Default.Edit,
                onTextSelected = {
                    onEvent(JobOfferEvent.TitleChanged(it))
                }
            )
            MyTextFieldComponent(
                labelValue = "Descripción",
                textValue = state.description,
                trailingIcon = Icons.AutoMirrored.Filled.Assignment,
                onTextSelected = {
                    onEvent(JobOfferEvent.DescriptionChanged(it))
                }
            )
            Text("Agregar evidencia", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            MultiplePhotoPickerComponent(
                selectedImages = state.images,
                onAddImage = { images ->
                    onEvent(JobOfferEvent.AddImage(images))
                },
                onRemoveImage = { index ->
                    onEvent(JobOfferEvent.RemoveImage(index))
                }
            )
            Text("Fecha", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            val dateOptions = listOf("Hoy", "3 dias", "1 semana", "Elegir")
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .defaultMinSize(minHeight = 20.dp)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                dateOptions.forEach { option ->
                    FilterChipComponent2(
                        text = option,
                        isSelected = state.selectedDateOption == option,
                        onClick = { onEvent(JobOfferEvent.DateOptionSelected(option)) }
                    )
                }
            }

            Text("Hora", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            val timeOptions = listOf("Mañana", "Tarde", "Noche", "Elegir")
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .defaultMinSize(minHeight = 20.dp)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                timeOptions.forEach { option ->
                    FilterChipComponent2(
                        text = option,
                        isSelected = state.selectedTimeOption == option,
                        onClick = { onEvent(JobOfferEvent.TimeOptionSelected(option)) }
                    )
                }
            }
            Row {
                Text("Fecha seleccionada: ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    state.requestedDate + " " + state.requestedTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
            ButtonComponent(
                value = "Publicar",
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    disabledContentColor = MaterialTheme.colorScheme.secondary
                ),
                onButtonClicked = {
                    onSubmit()
                }
            )

        }
        MyDialog2(
            title = "Oferta de trabajo publicada",
            show = state.isJobOfferSubmitted,
            onDismiss = onDismissSuccess,
            onConfirm = onNavigateSuccess
        ) {
            Text(text = "${state.successMessage}")
        }
    }
}

@Composable
private fun MapDialog(viewModel: JobOfferViewModel) {
    MyDialog2(
        title = "Seleccionar dirección",
        dismissTxt = "Cancelar",
        confirmTxt = "Aceptar",
        content = {
            MapScreen(
                modifier = Modifier
                    .padding(5.dp)
                    .width(306.dp)
                    .height(306.dp)
                    .clip(RoundedCornerShape(12.dp)),
                onMapClick = { latLng, address ->
                    viewModel.onEvent(
                        JobOfferEvent.LatitudeChanged(latLng.latitude)
                    )
                    viewModel.onEvent(
                        JobOfferEvent.LongitudeChanged(latLng.longitude)
                    )
                    viewModel.onEvent(
                        JobOfferEvent.AddressChanged(address)
                    )
                }
            )
        },
        show = viewModel.showMapDialog,
        onDismiss = { viewModel.showMapDialog = false },
        onConfirm = {
            viewModel.showMapDialog = false
        },
        modifier = Modifier.width(360.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun JobOfferScreenPreview() {
    Nexum_clienteTheme {
        Surface {
            val previewState = JobOfferState(
                address = "Calle Falsa 123, Springfield",
                title = "Reparación de tubería",
                description = "Se necesita reparar una tubería que gotea en la cocina.",
                selectedDateOption = "Hoy",
                selectedTimeOption = "Mañana",
                requestedDate = "08/17/2025",
                requestedTime = "Mañana"
            )

            JobOfferScreenContent(
                state = previewState,
                onEvent = {},
                onShowMapDialog = {},
                onSubmit = {}
            )
        }
    }
}
