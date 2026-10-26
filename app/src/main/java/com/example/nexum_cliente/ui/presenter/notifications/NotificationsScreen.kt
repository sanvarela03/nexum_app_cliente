package com.example.nexum_cliente.ui.presenter.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.nexum_cliente.data.notification.local.NotificationEntity
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme


@Composable
fun NotificationsScreen(
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onNavigateToApplications: (String) -> Unit = {}
) {
    val notifications =
        notificationViewModel.notifications.collectAsState(initial = emptyList()).value

    NotificationsScreenContent(
        notifications = notifications,
        onDelete = { notificationId ->
            notificationViewModel.onEvent(
                NotificationsEvent.DeleteBtnClick(
                    notificationId ?: -1
                )
            )
        },
        onClick = onNavigateToApplications
    )
}


@Composable
@Preview(name = "Notifications - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Notifications - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun NotificationsScreenContentPreview() {
    Nexum_clienteTheme {
        NotificationsScreenContent(
            notifications = listOf(
                NotificationEntity(
                    id = 1,
                    title = "Nueva postulación",
                    body = "Carlos Mendoza se ha postulado a tu oferta",
                    createdAt = "2026-09-09 03:30:00 PM",
                    type = "NEW_JOB_APPLICATION",
                    jobOfferId = "10",
                    jobOfferTitle = "Reparación de tubería",
                    workerName = "Carlos Mendoza",
                    totalCost = 65000.0,
                    locationName = "A 2.5 km"
                )
            )
        )
    }
}

@Composable
private fun NotificationsScreenContent(
    notifications: List<NotificationEntity>,
    onDelete: (Int) -> Unit = {},
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(notifications) { notification: NotificationEntity ->
                NotificationItem(
                    notification = notification,
                    onDelete = onDelete,
                    onClick = onClick
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun NotificationItem(
    notification: NotificationEntity,
    onDelete: (Int) -> Unit,
    onClick: (String) -> Unit
) {
    if (notification.type == "NEW_JOB_APPLICATION") {
        val fallbackUrl = remember(notification.workerName) {
            val name = (notification.workerName ?: "W").trim().replace("\\s+".toRegex(), "+")
            "https://ui-avatars.com/api/?name=$name&background=random&format=png"
        }
        val validImgUrl = notification.workerImageUrl?.takeIf {
            it.isNotBlank() && it != "NaN" && it != "null" && it.startsWith("http") && !it.contains("localhost") && !it.contains("127.0.0.1")
        }

        OutlinedCard(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    notification.jobOfferId?.let { onClick(it) }
                },
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlideImage(
                        model = validImgUrl ?: fallbackUrl,
                        contentDescription = "Avatar de ${notification.workerName}",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = notification.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        notification.workerName?.let {
                            Text(
                                text = "Postulante: $it",
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                    IconButton(onClick = { onDelete(notification.id ?: -1) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notification.body,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                if (notification.totalCost != null && notification.totalCost > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Presupuesto propuesto:",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "$${String.format("%,.0f", notification.totalCost)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF009963)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        text = notification.createdAt,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    } else {
        OutlinedCard(
            colors = outlinedCardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onDelete(notification.id ?: -1) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = notification.body, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        text = notification.createdAt,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
