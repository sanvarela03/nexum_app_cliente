package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.nexum_cliente.common.capitalizeFirstLetter
import com.example.nexum_cliente.common.formatToReadableDate
import com.example.nexum_cliente.common.toLocalDateTime
import com.example.nexum_cliente.data.review.remote.payload.res.ReviewRes
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.domain.model.JobOffer
import com.example.nexum_cliente.domain.model.MaterialItem
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobApplicationDetailDialog(
    application: JobApplication,
    jobOffer: JobOffer?,
    isAssigned: Boolean = false,
    reviews: List<ReviewRes> = emptyList(),
    isLoadingReviews: Boolean = false,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
    }

    val fallbackAvatarUrl = remember(application.workerName) {
        val parts = application.workerName.trim().split("\\s+".toRegex())
        val nameToUse = when (parts.size) {
            2 -> "${parts[0]}+${parts[1]}"
            3 -> "${parts[0]}+${parts[1]}"
            4 -> "${parts[0]}+${parts[2]}"
            else -> application.workerName.trim().replace("\\s+".toRegex(), "+").ifEmpty { "Worker" }
        }
        "https://ui-avatars.com/api/?name=$nameToUse&background=random&format=png"
    }

    val validImgUrl = application.workerImageUrl.takeIf {
        it.isNotBlank() && it != "NaN" && it != "null" && it.startsWith("http") && !it.contains("localhost") && !it.contains("127.0.0.1")
    }

    // Fecha de encuentro: de la propuesta o de la oferta
    val meetingDateText = remember(application.scheduledDateTime, jobOffer?.requestedDate) {
        val rawDate = application.scheduledDateTime.ifBlank { jobOffer?.requestedDate ?: "" }
        if (rawDate.isNotBlank()) {
            runCatching { rawDate.toLocalDateTime().formatToReadableDate() }.getOrDefault(rawDate)
        } else {
            "Por acordar con el cliente"
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header: Título y Cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Detalle de la Propuesta",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Cotización para oferta #${jobOffer?.id ?: application.jobOfferId}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF1F5F9), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(10.dp))

                // Contenido Scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. SECCIÓN: DATOS DEL TRABAJADOR
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF8FAFC)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlideImage(
                                    model = validImgUrl ?: fallbackAvatarUrl,
                                    contentDescription = "Foto de ${application.workerName}",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = application.workerName.ifEmpty { "Trabajador" },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Calificación
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB800),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        if (application.workerTotalReviews > 0) {
                                            Text(
                                                text = String.format(Locale.US, "%.1f", application.workerAverageRating),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF1E293B)
                                            )
                                            Text(
                                                text = "(${application.workerTotalReviews} opiniones)",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        } else {
                                            Text(
                                                text = "Nuevo en Nexum",
                                                fontSize = 12.sp,
                                                color = Color(0xFF64748B),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Distancia
                                    if (application.distanceInMeters.isNotBlank()) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = Color(0xFF009963),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = "A ${application.distanceInMeters} de tu ubicación",
                                                fontSize = 12.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }
                                    }
                                }
                            }

                            // Biografía del trabajador
                            val bioText = application.workerBio.takeIf { it.isNotBlank() && it != "null" }
                            if (bioText != null) {
                                HorizontalDivider(color = Color(0xFFE2E8F0))
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color(0xFF009963),
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = "Perfil del trabajador",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF1E293B)
                                        )
                                    }
                                    Text(
                                        text = bioText,
                                        fontSize = 13.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    // 2. SECCIÓN: DETALLES DE LA PROPUESTA (Mensaje y Fecha de Encuentro)
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Condiciones de la Oferta",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1E293B)
                            )

                            // Mensaje de la propuesta
                            if (application.proposalText.isNotBlank()) {
                                Surface(
                                    color = Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FormatQuote,
                                            contentDescription = null,
                                            tint = Color(0xFF009963),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Mensaje del trabajador:",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF334155)
                                            )
                                            Text(
                                                text = application.proposalText,
                                                fontSize = 13.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }
                                    }
                                }
                            }

                            // Fecha de encuentro
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Fecha de encuentro",
                                        tint = Color(0xFF009963),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Fecha y hora de encuentro / trabajo",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF00794F)
                                        )
                                        Text(
                                            text = meetingDateText,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1B5E20)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 3. SECCIÓN: DESGLOSE DE COSTOS Y MATERIALES
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Desglose Económico",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1E293B)
                            )

                            // Mano de Obra
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Handyman,
                                        contentDescription = null,
                                        tint = Color(0xFF009963),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Mano de Obra",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF334155)
                                    )
                                }
                                Text(
                                    text = currencyFormat.format(application.laborCost),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            HorizontalDivider(color = Color(0xFFF1F5F9))

                            // Materiales
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Build,
                                            contentDescription = null,
                                            tint = Color(0xFF009963),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "Materiales",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF334155)
                                        )
                                    }
                                    Text(
                                        text = currencyFormat.format(application.materialsCost),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (application.materialsCost > 0) Color(0xFF1E293B) else Color.Gray
                                    )
                                }

                                // Detalle de cada material si existe en la lista
                                if (application.materials.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 24.dp, top = 4.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        application.materials.forEach { item ->
                                            MaterialRowItem(item = item, currencyFormat = currencyFormat)
                                        }
                                    }
                                } else if (application.materialsCost == 0.0) {
                                    Text(
                                        text = "No incluye compra de materiales (el cliente los suministra o no se requieren).",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(start = 24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Total Cotizado Banner
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF0FDF4),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "TOTAL COTIZADO",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF166534)
                                        )
                                        Text(
                                            text = "Mano de obra + materiales",
                                            fontSize = 11.sp,
                                            color = Color(0xFF15803D)
                                        )
                                    }
                                    Text(
                                        text = currencyFormat.format(application.totalCost),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }
                        }
                    }

                    // 4. SECCIÓN: RESEÑAS Y OPINIONES
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Reseñas y Reputación",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1E293B)
                                )

                                if (application.workerTotalReviews > 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB800),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "${application.workerAverageRating} (${application.workerTotalReviews})",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    }
                                }
                            }

                            when {
                                isLoadingReviews -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = Color(0xFF009963)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Consultando opiniones de clientes...",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                                reviews.isNotEmpty() -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        reviews.take(5).forEach { review ->
                                            ReviewItemRow(review = review)
                                        }
                                    }
                                }
                                application.workerTotalReviews > 0 -> {
                                    Text(
                                        text = "El trabajador cuenta con ${application.workerTotalReviews} calificaciones con un promedio de ${application.workerAverageRating} estrellas en trabajos finalizados.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569)
                                    )
                                }
                                else -> {
                                    Text(
                                        text = "Este trabajador aún no tiene reseñas públicas en la plataforma.",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // 5. GARANTÍA ESCROW
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFF009963),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Garantía Nexum: Tu dinero queda en custodia segura hasta que confirmes la realización del trabajo.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(12.dp))

                // BOTÓN DE ACCIÓN INFERIOR
                if (isAssigned) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
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
                                text = "Propuesta Asignada a este Servicio",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF009963)
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            onDismiss()
                            onAccept()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009963),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Aceptar Propuesta (${currencyFormat.format(application.totalCost)})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialRowItem(
    item: MaterialItem,
    currencyFormat: NumberFormat
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.name.ifBlank { "Material" }.capitalizeFirstLetter(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    if (item.type.isNotBlank()) {
                        Surface(
                            color = Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = item.type,
                                fontSize = 10.sp,
                                color = Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "${item.quantity} und x ${currencyFormat.format(item.unitPrice)}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = currencyFormat.format(item.subtotal),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF009963)
            )
        }
    }
}

@Composable
private fun ReviewItemRow(review: ReviewRes) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.reviewerUsername ?: "Cliente verificado",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            if (!review.comment.isNullOrBlank()) {
                Text(
                    text = review.comment,
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
