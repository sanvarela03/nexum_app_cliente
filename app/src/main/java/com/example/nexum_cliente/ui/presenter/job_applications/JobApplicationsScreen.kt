package com.example.nexum_cliente.ui.presenter.job_applications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/6/2026
 * @version 1.0
 */

/* ── Stateful wrapper (ViewModel-aware) ── */

@Composable
fun JobApplicationsScreen(
    viewModel: JobApplicationsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToCheckout: (jobApplicationId: Long, agreedAmount: Double) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    JobApplicationsScreenContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onAcceptApplication = { application ->
            val id = application.jobApplicationId.toLongOrNull() ?: 0L
            val amount = application.totalCost
            onNavigateToCheckout(id, amount)
        }
    )
}

/* ── Stateless content (pure UI) ── */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JobApplicationsScreenContent(
    state: JobApplicationsState,
    onNavigateBack: () -> Unit = {},
    onAcceptApplication: (JobApplication) -> Unit = {}
) {
    val jobApplications = state.jobApplications

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Postulaciones Recibidas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "Error al cargar postulaciones",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (jobApplications.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "Aún no tienes postulaciones",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Los trabajadores cercanos notificados están revisando tu oferta. Las propuestas aparecerán aquí.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    item {
                        Text(
                            text = "Tienes ${jobApplications.size} propuesta(s) de trabajadores",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(jobApplications) { application ->
                        JobApplicationItem(
                            jobApplication = application,
                            onAccept = {
                                onAcceptApplication(application)
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ── Preview ── */

@Composable
@Preview(showBackground = true)
private fun JobApplicationsScreenContentPreview() {
    Nexum_clienteTheme {
        JobApplicationsScreenContent(
            state = JobApplicationsState(
                isLoading = false,
                jobApplications = listOf(
                    JobApplication(
                        jobApplicationId = "1",
                        jobOfferId = "10",
                        jobOfferTitle = "Reparación de tubería",
                        workerName = "Carlos Mendoza",
                        workerImageUrl = "",
                        locationName = "Usaquén, Bogotá",
                        distanceInMeters = "A 2.5 km",
                        workerBio = "Especialista en fontanería y plomería con más de 8 años de experiencia certificada.",
                        laborCost = 50000.0,
                        materialsCost = 15000.0,
                        totalCost = 65000.0,
                        workerAverageRating = 4.8,
                        workerTotalReviews = 24
                    ),
                    JobApplication(
                        jobApplicationId = "2",
                        jobOfferId = "10",
                        jobOfferTitle = "Reparación de tubería",
                        workerName = "María López García",
                        workerImageUrl = "",
                        locationName = "Chapinero, Bogotá",
                        distanceInMeters = "A 4.1 km",
                        workerBio = "Técnica certificada con experiencia en reparaciones residenciales.",
                        laborCost = 45000.0,
                        materialsCost = 20000.0,
                        totalCost = 65000.0,
                        workerAverageRating = 4.9,
                        workerTotalReviews = 38
                    )
                )
            )
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Empty State")
private fun JobApplicationsScreenEmptyPreview() {
    Nexum_clienteTheme {
        JobApplicationsScreenContent(
            state = JobApplicationsState(
                isLoading = false,
                jobApplications = emptyList()
            )
        )
    }
}

