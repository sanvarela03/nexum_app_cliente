package com.example.nexum_cliente.ui.presenter.requests

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nexum_cliente.common.capitalizeFirstLetter
import com.example.nexum_cliente.common.formatIsoDate
import com.example.nexum_cliente.common.formatToReadableDate
import com.example.nexum_cliente.common.toLocalDateTime
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.ui.components.Stepper
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme
import com.example.nexum_cliente.ui.theme.jobOfferCardBackground
import com.example.nexum_cliente.ui.theme.stepperColors
import com.example.nexum_cliente.ui.components.Stepper
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestsScreen(
    viewModel: RequestsViewModel = hiltViewModel(),
    navigateToTracking: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RequestsScreenContent(
        state = state,
        onRefresh = { viewModel.onEvent(RequestsEvent.Refresh(true)) },
        onSortChange = { viewModel.onEvent(RequestsEvent.ChangeSortType(it)) },
        onNavigateToTracking = navigateToTracking
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun RequestsScreenContent(
    state: RequestsState,
    onRefresh: () -> Unit,
    onSortChange: (RequestsSortType) -> Unit = {},
    onNavigateToTracking: (String) -> Unit = {}
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { onRefresh() },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        indicator = { refreshState, trigger ->
            SwipeRefreshIndicator(
                state = refreshState,
                refreshTriggerDistance = trigger,
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Barra de opciones de ordenamiento
            SortBar(
                currentSort = state.sortType,
                onSortChange = onSortChange
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                if (state.isLoading && state.jobOffers.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else if (state.jobOffers.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No tienes solicitudes activas",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    items(state.jobOffers, key = { it.id }) { jobOffer ->
                        JobOfferItem(
                            jobOffer = jobOffer,
                            onClick = { onNavigateToTracking(jobOffer.uuid) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SortBar(
    currentSort: RequestsSortType,
    onSortChange: (RequestsSortType) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Text(
                text = "Ordenar por:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
        item {
            FilterChip(
                selected = currentSort == RequestsSortType.DATE_DESC,
                onClick = { onSortChange(RequestsSortType.DATE_DESC) },
                label = { Text("Más reciente", fontSize = 12.sp) },
                leadingIcon = if (currentSort == RequestsSortType.DATE_DESC) {
                    {
                        Icon(
                            Icons.Default.ArrowDownward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
        item {
            FilterChip(
                selected = currentSort == RequestsSortType.DATE_ASC,
                onClick = { onSortChange(RequestsSortType.DATE_ASC) },
                label = { Text("Más antigua", fontSize = 12.sp) },
                leadingIcon = if (currentSort == RequestsSortType.DATE_ASC) {
                    {
                        Icon(
                            Icons.Default.ArrowUpward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
        item {
            FilterChip(
                selected = currentSort == RequestsSortType.STATUS,
                onClick = { onSortChange(RequestsSortType.STATUS) },
                label = { Text("Por estado", fontSize = 12.sp) },
                leadingIcon = if (currentSort == RequestsSortType.STATUS) {
                    {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JobOfferItem(
    jobOffer: JobOffer,
    onClick: () -> Unit = {}
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

    val currentStep = when (jobOffer.status.uppercase()) {
        "PENDING" -> 1
        "SEARCHING" -> 2
        "QUOTED" -> 3
        "ASSIGNED", "IN_PROGRESS" -> 4
        "COMPLETED" -> 5
        else -> 2
    }

    val locationText = if (!jobOffer.address.isNullOrBlank()) {
        jobOffer.address
    } else {
        "${jobOffer.location.lat}, ${jobOffer.location.lng}"
    }

    OutlinedCard(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.jobOfferCardBackground
        ),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stepper de progreso
            Stepper(
                numberOfSteps = 4,
                currentStep = currentStep,
                selectedColor = MaterialTheme.stepperColors.selected,
                unSelectedColor = MaterialTheme.stepperColors.unselected,
                selectedContentColor = MaterialTheme.stepperColors.onSelected
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Badge de Estado y UUID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "${jobOffer.uuid.take(8)}...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = 11.sp,
                )
            }

            // Título e ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jobOffer.title.capitalizeFirstLetter(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "#${jobOffer.id}",
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Descripción y Categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jobOffer.description.capitalizeFirstLetter(),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = jobOffer.categoryName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Fecha Requerida
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EditCalendar,
                    contentDescription = "Fecha solicitada",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = runCatching { jobOffer.requestedDate.toLocalDateTime().formatToReadableDate() }.getOrDefault(jobOffer.requestedDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Ubicación (Dirección o Coordenadas)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = locationText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Fecha de Creación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Creada: ${runCatching { jobOffer.createdAt.formatIsoDate() }.getOrDefault(jobOffer.createdAt)}",
                    fontWeight = FontWeight.Light,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(
    name = "Requests - Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Requests - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun RequestsScreenContentPreview() {
    Nexum_clienteTheme {
        RequestsScreenContent(
            state = RequestsState(
                jobOffers = listOf(
                    JobOffer(
                        title = "TEST1",
                    ),
                    JobOffer(
                        title = "Título 2",
                    )
                )
            ),
            onRefresh = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(
    name = "Requests - Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Requests - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun JobOfferItemPreview() {
    Nexum_clienteTheme() {
        JobOfferItem(
            jobOffer = JobOffer(),
        )
    }
}