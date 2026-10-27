package com.example.nexum_cliente.ui.presenter.job_offer

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Yard
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import com.example.nexum_cliente.common.MAX_JOB_OFFER_IMAGES
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.google.android.gms.maps.model.LatLng

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
    categoryName: String = "",
    viewModel: JobOfferViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
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

    LaunchedEffect(categoryId, categoryName) {
        viewModel.onEvent(JobOfferEvent.CategoryIdChanged(categoryId, categoryName))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotBlank()) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

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
        onNavigateBack = onNavigateBack,
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
        isValid = isFormValid,
        currentTimeMillis = System.currentTimeMillis()
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JobOfferScreenContent(
    state: JobOfferState,
    onEvent: (JobOfferEvent) -> Unit,
    onNavigateBack: () -> Unit = {},
    onShowMapDialog: () -> Unit,
    onMyLocationClick: () -> Unit,
    onSubmit: () -> Unit,
    onDismissSuccess: () -> Unit = {},
    onNavigateSuccess: () -> Unit = {},
    isValid: Boolean = false,
    currentTimeMillis: Long = 0L
) {
    val context = LocalContext.current
    Nexum_clienteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Nueva Solicitud",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        if (state.categoryName.isNotBlank()) {
                            CategoryBadge(
                                categoryName = state.categoryName,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Stepper fijo en la parte superior
                Stepper(
                    numberOfSteps = 4,
                    currentStep = 2,
                    selectedColor = Color(0xFF009963),
                    unSelectedColor = Color(0xFFE6E6E6),
                    modifier = Modifier.padding(5.dp)
                )

            // Contenido con scroll
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                                contentDescription = "Ubicación actual",
                                tint = MaterialTheme.colorScheme.secondary
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
                    textValue = state.title ?: "",
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
                // Sección de Evidencia con Indicador y Validaciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Agregar evidencia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    EvidenceCounterBadge(
                        currentCount = state.images.size,
                        maxImages = state.maxImages
                    )
                }

                val evidenceHelperText = when {
                    state.isAtImageLimit -> "Has alcanzado el límite máximo de ${state.maxImages} imágenes."
                    state.images.isNotEmpty() -> "Puedes subir hasta ${state.remainingImageSlots} imagen(es) más (máximo ${state.maxImages})."
                    else -> "Sube fotos como evidencia del trabajo requerido (máximo ${state.maxImages} imágenes)."
                }
                Text(
                    text = evidenceHelperText,
                    fontSize = 12.sp,
                    color = if (state.isAtImageLimit) Color(0xFFE65100) else Color(0xFF757575)
                )

                if (state.isUploadingImage) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF009963)
                        )
                        Text(
                            text = "Subiendo imagen(es) a la nube (${state.uploadingCount} pendiente${if (state.uploadingCount > 1) "s" else ""})...",
                            fontSize = 12.sp,
                            color = Color(0xFF009963)
                        )
                    }
                }

                MultiplePhotoPickerComponent(
                    selectedImages = state.images,
                    maxImages = state.maxImages,
                    onAddImage = { uris ->
                        uris.forEach { uri ->
                            onEvent(JobOfferEvent.ImageUriAdded(uri))
                        }
                    },
                    onRemoveImage = { index ->
                        val url = state.imageUrls.getOrNull(index)
                        if (url != null) {
                            onEvent(JobOfferEvent.ImageUrlRemoved(url))
                        }
                        onEvent(JobOfferEvent.RemoveImage(index))
                    },
                    onLimitExceeded = {
                        Toast.makeText(
                            context,
                            "Solo puedes subir un máximo de ${state.maxImages} imágenes.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                if (!state.imagesValidation.isValid && state.images.isNotEmpty()) {
                    Text(
                        text = state.imagesValidation.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
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
                        val isEnabled = remember(option, currentTimeMillis) {
                            when (option) {
                                "Hoy" -> JobOfferValidator.isTodayAllowed()
                                else -> true
                            }
                        }
                        FilterChipComponent2(
                            text = option,
                            isSelected = state.selectedDateOption == option,
                            enabled = isEnabled,
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
                                "Mañana" -> JobOfferValidator.validateTime(
                                    10,
                                    0,
                                    state.requestedDate
                                ).isValid

                                "Tarde" -> JobOfferValidator.validateTime(
                                    18,
                                    0,
                                    state.requestedDate
                                ).isValid

                                "Noche" -> JobOfferValidator.validateTime(
                                    22,
                                    0,
                                    state.requestedDate
                                ).isValid

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

                    val isTimeValid =
                        remember(state.requestedDate, state.requestedTime, currentTimeMillis) {
                            try {
                                val timeFormatter =
                                    java.text.SimpleDateFormat("hh:mm a", java.util.Locale.ENGLISH)
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
                // Espacio extra al final para que el botón no quede pegado al borde inferior
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
            }
        }
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

@Composable
fun CategoryBadge(
    categoryName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE8F5E9),
        border = BorderStroke(1.dp, Color(0xFF009963).copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = getCategoryIcon(categoryName),
                contentDescription = categoryName,
                tint = Color(0xFF009963),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = categoryName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF009963)
            )
        }
    }
}

/**
 * Mapea el nombre de la categoría a su respectivo icono vector de Compose Material
 */
fun getCategoryIcon(categoryName: String): ImageVector {
    val normalized = categoryName.trim().lowercase()
    return when {
        normalized.contains("electri") || normalized.contains("luz") || normalized.contains("cable") -> Icons.Default.Bolt
        normalized.contains("plom") || normalized.contains("fontan") || normalized.contains("agua") || normalized.contains("tub") || normalized.contains("grifo") -> Icons.Default.Plumbing
        normalized.contains("cerraj") || normalized.contains("llav") || normalized.contains("candado") || normalized.contains("cerrad") -> Icons.Default.Key
        normalized.contains("carpin") || normalized.contains("mader") || normalized.contains("muebl") -> Icons.Default.Carpenter
        normalized.contains("pint") -> Icons.Default.FormatPaint
        normalized.contains("limp") || normalized.contains("aseo") || normalized.contains("desinfec") -> Icons.Default.CleaningServices
        normalized.contains("constr") || normalized.contains("albañ") || normalized.contains("remodel") -> Icons.Default.Construction
        normalized.contains("jard") || normalized.contains("plant") || normalized.contains("pasto") || normalized.contains("cesped") -> Icons.Default.Yard
        normalized.contains("aire") || normalized.contains("clima") || normalized.contains("refrig") -> Icons.Default.AcUnit
        normalized.contains("mecan") || normalized.contains("auto") || normalized.contains("vehic") || normalized.contains("moto") -> Icons.Default.CarRepair
        normalized.contains("electro") || normalized.contains("tecn") || normalized.contains("comput") -> Icons.Default.HomeRepairService
        else -> Icons.Default.Handyman
    }
}

@Composable
fun EvidenceCounterBadge(
    currentCount: Int,
    maxImages: Int = MAX_JOB_OFFER_IMAGES,
    modifier: Modifier = Modifier
) {
    val isLimitReached = currentCount >= maxImages
    val hasImages = currentCount > 0

    val backgroundColor = when {
        isLimitReached -> Color(0xFFFFF3E0) // Cálido cuando llega al límite
        hasImages -> Color(0xFFE8F5E9)      // Verde claro de la app cuando tiene fotos
        else -> Color(0xFFF5F5F5)           // Gris neutro cuando está vacío
    }

    val contentColor = when {
        isLimitReached -> Color(0xFFE65100) // Ámbar/Naranja
        hasImages -> Color(0xFF009963)      // Verde Nexum
        else -> Color(0xFF757575)           // Gris suave
    }

    val borderColor = when {
        isLimitReached -> Color(0xFFFFB74D)
        hasImages -> Color(0xFF009963).copy(alpha = 0.4f)
        else -> Color(0xFFE0E0E0)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (isLimitReached) Icons.Default.CheckCircle else Icons.Default.PhotoCamera,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = if (isLimitReached) "$currentCount / $maxImages (Máx)" else "$currentCount / $maxImages",
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimePickerDialogPreview() {
    Nexum_clienteTheme {
        TimePickerDialogContent(selectedDate = "01/01/2026", initialHour = 12, initialMinute = 0)
    }
}


@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun JobOfferScreenPreview() {
    Nexum_clienteTheme {
        val previewState = JobOfferState(
            address = "Calle Falsa 123, Springfield",
            title = "Reparación de tubería",
            description = "Se necesita reparar una tubería que gotea en la cocina.",
            selectedDateOption = "Hoy",
            selectedTimeOption = "Mañana",
            requestedDate = "08/17/2025",
            requestedTime = "Mañana",
            categoryName = "Plomería"
        )
        JobOfferScreenContent(
            state = previewState,
            onEvent = {},
            onNavigateBack = {},
            onShowMapDialog = {},
            onMyLocationClick = {},
            onSubmit = {}
        )
    }
}