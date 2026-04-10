package com.example.nexum_cliente.ui.presenter.requests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_cliente.common.capitalizeFirstLetter
import com.example.nexum_cliente.common.formatIsoDate
import com.example.nexum_cliente.common.formatToReadableDate
import com.example.nexum_cliente.common.toLocalDateTime
import com.example.nexum_cliente.domain.model.JobOffer
import com.google.accompanist.swiperefresh.SwipeRefresh
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
    val state = viewModel.state
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
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(state.jobOffers.size) { i ->
                val jobOffer = state.jobOffers[i]
                JobOfferItem(
                    jobOffer = jobOffer,
                    onClick = { onNavigateToTracking(jobOffer.uuid) }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RequestsScreenContentPreview() {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JobOfferItem(
    jobOffer: JobOffer,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
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
            com.example.nexum_cliente.ui.components.Stepper(
                numberOfSteps = 4,
                currentStep = when(jobOffer.status) {
                    "PENDING", "SEARCHING" -> 2
                    "QUOTED" -> 3
                    "ASSIGNED", "IN_PROGRESS" -> 4
                    else -> 2
                },
                selectedColor = androidx.compose.ui.graphics.Color(0xFF009963),
                unSelectedColor = androidx.compose.ui.graphics.Color(0xFFE6E6E6)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${jobOffer.uuid}",
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
            Row() {
                Text(
                    text = "${jobOffer.title.capitalizeFirstLetter()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${jobOffer.id}",
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
            Row() {
                Text(
                    text = jobOffer.description.capitalizeFirstLetter(),
                    modifier = Modifier.weight(1f)
                )
                Text(text = jobOffer.categoryName)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EditCalendar,
                    contentDescription = "Location Icon"
                )
                Text(
                    text = "${jobOffer.requestedDate.toLocalDateTime().formatToReadableDate()}"
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon"
                )
                Text(
                    text = "${jobOffer.location.lat}, ${jobOffer.location.lng}",
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )


            }
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${jobOffer.createdAt.formatIsoDate()}",
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
        }
    }
}