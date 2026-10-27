package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nexum_cliente.common.capitalizeFirstLetter
import com.example.nexum_cliente.common.formatToReadableDate
import com.example.nexum_cliente.common.toLocalDateTime
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.model.NearbyWorker
import com.example.nexum_cliente.ui.components.Stepper
import com.example.nexum_cliente.ui.presenter.job_applications.JobApplicationItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobOfferTrackingScreen(
    jobOfferId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: (jobApplicationId: Long, agreedAmount: Double) -> Unit = { _, _ -> },
    viewModel: JobOfferTrackingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(onBack = onNavigateBack)

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    val offerStatus = state.jobOffer?.status?.uppercase()
    val isAssigned = offerStatus in listOf("ASSIGNED", "IN_PROGRESS", "COMPLETED") || state.hasExistingContract

    // Progreso del stepper sincronizado con el estado real de la oferta
    val currentStep = when {
        offerStatus == "COMPLETED" -> 5
        offerStatus in listOf("ASSIGNED", "IN_PROGRESS") || state.hasExistingContract -> 4
        offerStatus == "PENDING" -> 1
        offerStatus == "SEARCHING" -> if (state.applications.isNotEmpty()) 3 else 2
        offerStatus == "QUOTED" -> 3
        else -> if (state.applications.isNotEmpty()) 3 else 2
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento de Oferta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(JobOfferTrackingEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Stepper con progreso sincronizado
                Stepper(
                    numberOfSteps = 4,
                    currentStep = currentStep,
                    selectedColor = Color(0xFF009963),
                    unSelectedColor = Color(0xFFE6E6E6)
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. Tarjeta informativa de la oferta que se está consultando
                    item {
                        if (state.jobOffer != null) {
                            ConsultedJobOfferCard(jobOffer = state.jobOffer!!)
                        } else {
                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFF8FAFC))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = Color(0xFF009963)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Cargando información de la oferta...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // 2. Encabezado contextual según el estado
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(2.dp))
                            when {
                                isAssigned -> {
                                    Text(
                                        text = "¡Trabajador Asignado!",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF009963),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Has seleccionado una propuesta para este servicio. El trabajo se encuentra asignado y en proceso.",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                state.applications.isNotEmpty() -> {
                                    Text(
                                        text = "¡Cotizaciones Recibidas (${state.applications.size})!",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF009963),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Trabajadores disponibles han postulado a tu oferta con su propuesta de trabajo. Revisa los detalles y acepta la que prefieras.",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                else -> {
                                    Text(
                                        text = "Buscando trabajadores...",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tu oferta de trabajo ha sido publicada. Estamos notificando a los trabajadores cercanos. Pronto recibirás cotizaciones aquí.",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // 3. Error si lo hay
                    if (state.errorMessage.isNotEmpty()) {
                        item {
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // 4. Lista de postulaciones / cotizaciones
                    if (state.applications.isNotEmpty()) {
                        items(state.applications) { application ->
                            JobApplicationItem(
                                jobApplication = application,
                                isAssigned = isAssigned,
                                onClick = {
                                    viewModel.onEvent(JobOfferTrackingEvent.SelectApplication(application))
                                },
                                onAccept = {
                                    val id = application.jobApplicationId.toLongOrNull() ?: 0L
                                    val amount = application.totalCost
                                    onNavigateToCheckout(id, amount)
                                }
                            )
                        }

                        // Trabajadores cercanos notificados
                        if (state.workers.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Trabajadores cercanos notificados (${state.workers.size})",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(state.workers) { worker ->
                                WorkerItem(worker = worker)
                            }
                        }
                    } else if (state.workers.isNotEmpty()) {
                        item {
                            Text(
                                text = "¡Hemos notificado a ${state.workers.size} trabajadores cerca de ti!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF009963),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                        items(state.workers) { worker ->
                            WorkerItem(worker = worker)
                        }
                    }

                    // Footer
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Puedes volver atrás o revisar esta vista desde 'Mis Solicitudes'.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }

        // Diálogo con el detalle completo de la propuesta y trabajador
        state.selectedApplication?.let { selectedApp ->
            JobApplicationDetailDialog(
                application = selectedApp,
                jobOffer = state.jobOffer,
                isAssigned = isAssigned,
                reviews = state.workerReviews,
                isLoadingReviews = state.isLoadingReviews,
                onAccept = {
                    val id = selectedApp.jobApplicationId.toLongOrNull() ?: 0L
                    val amount = selectedApp.totalCost
                    onNavigateToCheckout(id, amount)
                },
                onDismiss = {
                    viewModel.onEvent(JobOfferTrackingEvent.DismissApplicationDetail)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConsultedJobOfferCard(
    jobOffer: JobOffer,
    modifier: Modifier = Modifier
) {
    val (statusLabel, statusColor, statusBg) = when (jobOffer.status.uppercase()) {
        "PENDING" -> Triple("Pendiente", Color(0xFF6C757D), Color(0xFFF1F3F5))
        "SEARCHING" -> Triple("Buscando trabajadores", Color(0xFF0D6EFD), Color(0xFFE7F1FF))
        "QUOTED" -> Triple("Cotizaciones recibidas", Color(0xFFD97706), Color(0xFFFEF3C7))
        "ASSIGNED" -> Triple("Trabajador asignado", Color(0xFF009963), Color(0xFFE6F4EA))
        "IN_PROGRESS" -> Triple("En progreso", Color(0xFF0D9488), Color(0xFFCCFBF1))
        "COMPLETED" -> Triple("Completado", Color(0xFF16A34A), Color(0xFFDCFCE7))
        "CANCELLED" -> Triple("Cancelado", Color(0xFFDC2626), Color(0xFFFEE2E2))
        else -> Triple(jobOffer.status, Color.Gray, Color(0xFFF1F3F5))
    }

    val locationText = if (!jobOffer.address.isNullOrBlank()) {
        jobOffer.address
    } else {
        "${jobOffer.location.lat}, ${jobOffer.location.lng}"
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color(0xFFF8FAFC)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Status Badge + UUID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "${jobOffer.uuid.take(8)}...",
                    fontWeight = FontWeight.Light,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Título e ID (#47)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jobOffer.title.capitalizeFirstLetter(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "#${jobOffer.id}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Descripción y Categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (jobOffer.description.isNotBlank()) {
                    Text(
                        text = jobOffer.description.capitalizeFirstLetter(),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )
                }
                if (jobOffer.categoryName.isNotBlank()) {
                    if (jobOffer.description.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = jobOffer.categoryName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 0.8.dp)

            // Fecha solicitada
            if (jobOffer.requestedDate.isNotBlank()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EditCalendar,
                        contentDescription = "Fecha solicitada",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = runCatching { jobOffer.requestedDate.toLocalDateTime().formatToReadableDate() }.getOrDefault(jobOffer.requestedDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF334155)
                    )
                }
            }

            // Ubicación / Dirección
            if (locationText.isNotBlank()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF334155),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun WorkerItem(worker: NearbyWorker) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color(0xFFF9F9F9),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            com.example.nexum_cliente.ui.components.UserAvatar(
                imageUrl = worker.profileImageUrl,
                name = "${worker.firstName} ${worker.lastName}",
                size = 46.dp,
                contentDescription = "Avatar de ${worker.firstName}"
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${worker.firstName} ${worker.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (worker.bio.isNotBlank() && worker.bio != "null") worker.bio else "Trabajador disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (worker.distanceInMeters >= 1000) {
                        String.format("%.1f km", worker.distanceInMeters / 1000.0)
                    } else {
                        "${worker.distanceInMeters.toInt()} m"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF009963)
                )
                Text(
                    text = "Distancia",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
