package com.example.nexum_cliente.ui.presenter.requests

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nexum_cliente.common.capitalizeFirstLetter
import com.example.nexum_cliente.common.formatIsoDate
import com.example.nexum_cliente.common.formatToReadableDate
import com.example.nexum_cliente.common.toLocalDateTime
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.ui.components.Stepper
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme
import com.example.nexum_cliente.ui.theme.stepperColors
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
        onNavigateToTracking = navigateToTracking
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun RequestsScreenContent(
    state: RequestsState,
    onRefresh: () -> Unit,
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
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
                items(state.jobOffers) { jobOffer ->
                    JobOfferItem(
                        jobOffer = jobOffer,
                        onClick = { onNavigateToTracking(jobOffer.uuid) }
                    )
                }
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
private fun JobOfferItem(
    jobOffer: JobOffer,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Stepper(
                numberOfSteps = 4,
                currentStep = when(jobOffer.status) {
                    "PENDING", "SEARCHING" -> 2
                    "QUOTED" -> 3
                    "ASSIGNED", "IN_PROGRESS" -> 4
                    else -> 2
                },
                selectedColor = MaterialTheme.stepperColors.selected,
                unSelectedColor = MaterialTheme.stepperColors.unselected,
                selectedContentColor = MaterialTheme.stepperColors.onSelected
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${jobOffer.uuid}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
            Row() {
                Text(
                    text = "${jobOffer.title.capitalizeFirstLetter()}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${jobOffer.id}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
            Row() {
                Text(
                    text = jobOffer.description.capitalizeFirstLetter(),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = jobOffer.categoryName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EditCalendar,
                    contentDescription = "Location Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${jobOffer.requestedDate.toLocalDateTime().formatToReadableDate()}",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${jobOffer.location.lat}, ${jobOffer.location.lng}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )


            }
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${jobOffer.createdAt.formatIsoDate()}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
        }
    }
}
