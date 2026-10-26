package com.example.nexum_cliente.ui.presenter.job_applications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.nexum_cliente.R
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme
import com.example.nexum_cliente.utils.DistanceUtils

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/6/2026
 * @version 1.0
 */
@Composable
fun JobApplicationItem(
    jobApplication: JobApplication,
    onAccept: () -> Unit = {},
    onClick: () -> Unit = {},
    isAssigned: Boolean = false,
    modifier: Modifier = Modifier
) {
    JobApplicationItemContent(
        jobApplication = jobApplication,
        onAccept = onAccept,
        onClick = onClick,
        isAssigned = isAssigned,
        modifier = modifier
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun JobApplicationItemContent(
    jobApplication: JobApplication,
    onAccept: () -> Unit = {},
    onClick: () -> Unit = {},
    isAssigned: Boolean = false,
    modifier: Modifier = Modifier
) {
    val fallbackUrl = remember(jobApplication.workerName) {
        val parts = jobApplication.workerName.trim().split("\\s+".toRegex())
        val nameToUse = when (parts.size) {
            2 -> "${parts[0]}+${parts[1]}"
            3 -> "${parts[0]}+${parts[1]}"
            4 -> "${parts[0]}+${parts[2]}"
            else -> jobApplication.workerName.trim().replace("\\s+".toRegex(), "+").ifEmpty { "Worker" }
        }
        "https://ui-avatars.com/api/?name=$nameToUse&background=random&format=png"
    }

    val validImgUrl = jobApplication.workerImageUrl.takeIf {
        it.isNotBlank() && it != "NaN" && it != "null" && it.startsWith("http") && !it.contains("localhost") && !it.contains("127.0.0.1")
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() },
        colors = outlinedCardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Cabecera: Avatar + Nombre + Distancia
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = validImgUrl ?: fallbackUrl,
                    contentDescription = "Foto de ${jobApplication.workerName}",
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = jobApplication.workerName.ifEmpty { "Trabajador" },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Calificación",
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(15.dp)
                            )
                            if (jobApplication.workerTotalReviews > 0) {
                                Text(
                                    text = String.format(java.util.Locale.US, "%.1f (%d)", jobApplication.workerAverageRating, jobApplication.workerTotalReviews),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B)
                                )
                            } else {
                                Text(
                                    text = "Nuevo",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Distancia
                        val displayDistance = remember(jobApplication.distanceInMeters) {
                            DistanceUtils.formatDistance(jobApplication.distanceInMeters)
                        }
                        if (displayDistance.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF009963),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = displayDistance,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Biografía / Descripción de la propuesta
            val displayDesc = jobApplication.proposalText.ifBlank { jobApplication.workerBio }.takeIf { it.isNotBlank() && it != "null" }
            if (displayDesc != null) {
                Text(
                    text = displayDesc,
                    fontSize = 14.sp,
                    color = Color(0xFF475569),
                    lineHeight = 19.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            // Desglose de Costos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mano de obra: $${String.format("%,.0f", jobApplication.laborCost)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    if (jobApplication.materialsCost > 0) {
                        Text(
                            text = "Materiales: $${String.format("%,.0f", jobApplication.materialsCost)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total cotizado",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$${String.format("%,.0f", jobApplication.totalCost)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF009963)
                    )
                }
            }

            // Indicador interactivo para ver detalle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Toca para ver detalles de la oferta y trabajador",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF009963)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF009963),
                    modifier = Modifier.size(13.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Botón de acción
            if (isAssigned) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF009963),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Propuesta Asignada",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF009963)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF009963),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Aceptar Propuesta",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun JobApplicationItemPreview() {
    Nexum_clienteTheme {
        JobApplicationItemContent(
            jobApplication = JobApplication(
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
            )
        )
    }
}
