package com.example.nexum_cliente.ui.presenter.job_offer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
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
import com.example.nexum_cliente.utils.validator.JobOfferValidator

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 4.8
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobOfferScreen(
    categoryId: Long,
    viewModel: JobOfferViewModel = hiltViewModel(),
    onNavigateToTracking: (String) -> Unit = {}
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        if (granted) {
            viewModel.onEvent(JobOfferEvent.RequestCurrentLocation)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(JobOfferEvent.CategoryIdChanged(categoryId))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    // El indicador de validez ahora viene directamente del estado
    val isFormValid = state.isFormValid

    MapDialog(viewModel)

    if (state.showDatePickerDialog) {
        Nexum_clienteTheme {
            DatePickerDialogContent(
                initialDateMillis = viewModel.getInitialDateMillis(),
                onDismissRequest = { viewModel.onEvent(JobOfferEvent.ShowDatePicker(false)) },
                onConfirm = {
                    viewModel.onEvent(JobOfferEvent.DateSelected(it))
                    viewModel.onEvent(JobOfferEvent.ShowDatePicker(false))
                }
            )
        }
    }

    if (state.showTimePickerDialog) {
        Nexum_clienteTheme {
            TimePickerDialogContent(
                selectedDate = state.requestedDate,
                initialHour = viewModel.getInitialTimeValues()[0],
                initialMinute = viewModel.getInitialTimeValues()[1],
                onDismissRequest = { viewModel.onEvent(JobOfferEvent.ShowTimePicker(false)) },
                onConfirm = { hour, minute ->
                    viewModel.onEvent(JobOfferEvent.TimeSelected(hour, minute))
                    viewModel.onEvent(JobOfferEvent.ShowTimePicker(false))
                }
            )
        }
    }

    JobOfferScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onShowMapDialog = { viewModel.showMapDialog = true },
        onMyLocationClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.onEvent(JobOfferEvent.RequestCurrentLocation)
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        },
        onSubmit = { viewModel.onEvent(JobOfferEvent.Submit) },
        onDismissSuccess = { viewModel.onEvent(JobOfferEvent.DismissSuccessDialog) },
        onNavigateSuccess = {
            viewModel.onEvent(JobOfferEvent.DismissSuccessDialog)
            onNavigateToTracking(state.createdJobOfferUuid.ifEmpty { "N/A" })
        },
        isValid = isFormValid
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialogContent(
    initialDateMillis: Long? = null,
    colors: DatePickerColors = DatePickerDefaults.colors(
        containerColor = Color.White,
        titleContentColor = Color.Black,
        headlineContentColor = Color.Black,
        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
    ),
    onDismissRequest: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return JobOfferValidator.isDateSelectable(utcTimeMillis)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return JobOfferValidator.isYearSelectable(year)
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onConfirm(it)
                }
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = colors
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DatePickerDialogContentPreview() {
    Nexum_clienteTheme {
        DatePickerDialogContent(
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerDialogContent(
    selectedDate: String,
    initialHour: Int,
    initialMinute: Int,
    colors: TimePickerColors = TimePickerDefaults.colors(
        clockDialColor = MaterialTheme.colorScheme.surface,
        clockDialUnselectedContentColor = Color.Black,
        containerColor = Color.White,
        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.surface,
        periodSelectorUnselectedContainerColor = Color.White,
        timeSelectorSelectedContainerColor = Color.LightGray,
        timeSelectorUnselectedContainerColor = Color.White,
    ),
    onDismissRequest: () -> Unit = {},
    onConfirm: (Int, Int) -> Unit = { _, _ -> }
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute
    )

    val validationResult = remember(timePickerState.hour, timePickerState.minute, selectedDate) {
        JobOfferValidator.validateTime(timePickerState.hour, timePickerState.minute, selectedDate)
    }

    TimePickerDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        onConfirm = {
            if (validationResult.isValid) {
                onConfirm(timePickerState.hour, timePickerState.minute)
            }
        },
        confirmEnabled = validationResult.isValid
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TimePicker(
                state = timePickerState,
                colors = colors
            )
            if (!validationResult.isValid) {
                Text(
                    text = validationResult.errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobOfferScreenContent(
    state: JobOfferState,
    onEvent: (JobOfferEvent) -> Unit,
    onShowMapDialog: () -> Unit,
    onMyLocationClick: () -> Unit,
    onSubmit: () -> Unit,
    onDismissSuccess: () -> Unit = {},
    onNavigateSuccess: () -> Unit = {},
    isValid: Boolean = false,
    currentTimeMillis: Long = 0L
) {
    Nexum_clienteTheme {
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
                errorStatus = !state.addressValidation.isValid,
                errorMessage = state.addressValidation.errorMessage,
                leadingIcon = {
                    IconButton(onClick = onMyLocationClick) {
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
                errorStatus = !state.titleValidation.isValid,
                errorMessage = state.titleValidation.errorMessage,
                onTextSelected = {
                    onEvent(JobOfferEvent.TitleChanged(it))
                }
            )
            MyTextFieldComponent(
                labelValue = "Descripción",
                textValue = state.description,
                trailingIcon = Icons.AutoMirrored.Filled.Assignment,
                errorStatus = !state.descriptionValidation.isValid,
                errorMessage = state.descriptionValidation.errorMessage,
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
                    val isEnabled = remember(state.requestedDate, option, currentTimeMillis) {
                        when (option) {
                            "Mañana" -> JobOfferValidator.validateTime(10, 0, state.requestedDate).isValid
                            "Tarde" -> JobOfferValidator.validateTime(18, 0, state.requestedDate).isValid
                            "Noche" -> JobOfferValidator.validateTime(22, 0, state.requestedDate).isValid
                            else -> true
                        }
                    }
                    FilterChipComponent2(
                        text = option,
                        isSelected = state.selectedTimeOption == option,
                        enabled = isEnabled,
                        onClick = { onEvent(JobOfferEvent.TimeOptionSelected(option)) }
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Fecha seleccionada: ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                
                val isTimeValid = remember(state.requestedDate, state.requestedTime, currentTimeMillis) {
                    try {
                        val timeFormatter = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.ENGLISH)
                        val date = timeFormatter.parse(state.requestedTime)
                        val cal = java.util.Calendar.getInstance().apply { 
                            if (date != null) time = date 
                        }
                        JobOfferValidator.validateTime(
                            cal.get(java.util.Calendar.HOUR_OF_DAY),
                            cal.get(java.util.Calendar.MINUTE),
                            state.requestedDate
                        ).isValid
                    } catch (e: Exception) {
                        false
                    }
                }
                
                Text(
                    text = "${state.requestedDate} ${state.requestedTime}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = if (isTimeValid) Color(0xFF009963) else Color.Red
                )
            }
            ButtonComponent(
                value = "Publicar",
                isEnabled = isValid,
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
            Text(text = state.successMessage)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MapDialog(viewModel: JobOfferViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MyDialog2(
        title = "Seleccionar dirección",
        dismissTxt = "Cancelar",
        confirmTxt = "Aceptar",
        content = {
            val initialLocation = if (state.latitude != 0.0 && state.longitude != 0.0) {
                LatLng(state.latitude, state.longitude)
            } else null

            MapScreen(
                modifier = Modifier
                    .padding(5.dp)
                    .width(306.dp)
                    .height(306.dp)
                    .clip(RoundedCornerShape(12.dp)),
                initialLocation = initialLocation,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimePickerDialogPreview() {
    Nexum_clienteTheme {
        TimePickerDialogContent(selectedDate = "01/01/2026", initialHour = 12, initialMinute = 0)
    }
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
                onMyLocationClick = {},
                onSubmit = {}
            )
        }
    }
}