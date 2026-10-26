package com.example.nexum_cliente.ui.presenter.contracts

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.ui.presenter.review.ReviewDialog
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla de gestión de citas y contratos para el cliente de Nexum.
 *
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
@Composable
fun ContractsScreen(
    viewModel: ContractsViewModel = hiltViewModel(),
    onOpenChat: (workerUserId: String, workerRole: String) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.initPoint) {
        state.initPoint?.let { url ->
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
                .launchUrl(context, Uri.parse(url))
            viewModel.onEvent(ContractsEvent.ResetInitPoint)
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(ContractsEvent.ClearMessages)
        }
    }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(ContractsEvent.ClearMessages)
        }
    }

    // Modal para mostrar el PIN de verificación generado
    if (state.showCodeDialog && state.generatedCode != null && state.selectedContract != null) {
        VerificationCodeDisplayDialog(
            contract = state.selectedContract,
            code = state.generatedCode,
            onDismiss = { viewModel.onEvent(ContractsEvent.DismissCodeDialog) }
        )
    }

    // Modal para calificar al trabajador
    if (state.showReviewDialog && state.contractToReview != null) {
        val targetName = state.contractToReview.workerName ?: "el trabajador"
        ReviewDialog(
            targetName = targetName,
            targetRole = "el trabajador",
            onSubmit = { rating, comment ->
                viewModel.onEvent(ContractsEvent.SubmitReview(rating, comment))
            },
            onDismiss = { viewModel.onEvent(ContractsEvent.DismissReviewDialog) }
        )
    }

    // Modal para pagar la mano de obra
    if (state.showPaymentDialog && state.contractToPay != null) {
        LaborPaymentDialog(
            contract = state.contractToPay,
            isPaying = state.isPayingLabor,
            onConfirm = { method ->
                viewModel.onEvent(ContractsEvent.ConfirmPayLabor(state.contractToPay, method))
            },
            onDismiss = { viewModel.onEvent(ContractsEvent.DismissPaymentDialog) }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mis Citas y Contratos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Genera códigos presenciales y califica servicios",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { viewModel.onEvent(ContractsEvent.RefreshContracts) },
                    enabled = !state.isRefreshing
                ) {
                    if (state.isRefreshing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips
            FilterSection(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { viewModel.onEvent(ContractsEvent.SelectFilter(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista filtrada
            val filteredContracts = remember(state.contracts, state.selectedFilter) {
                when (state.selectedFilter) {
                    "SCHEDULED" -> state.contracts.filter {
                        it.currentStatus == "MEETING_SCHEDULED" || it.currentStatus == "CREATED"
                    }
                    "VERIFIED" -> state.contracts.filter {
                        it.currentStatus == "VERIFIED" || it.currentStatus == "INVOICED" || it.currentStatus == "ACTIVE"
                    }
                    "COMPLETED" -> state.contracts.filter {
                        it.currentStatus == "COMPLETED_BY_WORKER" || it.currentStatus == "COMPLETED"
                    }
                    else -> state.contracts
                }
            }

            if (state.isLoading && state.contracts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (filteredContracts.isEmpty()) {
                EmptyContractsView(
                    filter = state.selectedFilter,
                    onRefresh = { viewModel.onEvent(ContractsEvent.RefreshContracts) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredContracts, key = { it.id }) { contract ->
                        ClientContractCard(
                            contract = contract,
                            isGeneratingCode = state.isGeneratingCode && state.selectedContract?.id == contract.id,
                            onGenerateCodeClick = { viewModel.onEvent(ContractsEvent.GenerateVerificationCode(contract)) },
                            onPayLaborClick = { viewModel.onEvent(ContractsEvent.OpenPaymentDialog(contract)) },
                            onReviewClick = { viewModel.onEvent(ContractsEvent.OpenReviewDialog(contract)) },
                            onOpenChatClick = {
                                val workerUserId = contract.workerUserId?.toString()
                                if (workerUserId != null) {
                                    onOpenChat(workerUserId, "WORKER")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf(
        "ALL" to "Todas",
        "SCHEDULED" to "Por atender",
        "VERIFIED" to "En curso",
        "COMPLETED" to "Finalizadas"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(filters) { (key, label) ->
            val isSelected = selectedFilter == key
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(key) },
                label = { Text(label, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ClientContractCard(
    contract: ContractRes,
    isGeneratingCode: Boolean,
    onGenerateCodeClick: () -> Unit,
    onPayLaborClick: () -> Unit,
    onReviewClick: () -> Unit,
    onOpenChatClick: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Contrato ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Contrato #${contract.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ClientStatusChip(status = contract.currentStatus)
            }

            // Datos del Trabajador
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val workerName = contract.workerName ?: "Trabajador asignado"
                com.example.nexum_cliente.ui.components.UserAvatar(
                    imageUrl = contract.workerImageUrl,
                    name = workerName,
                    size = 46.dp
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workerName,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = contract.jobOfferTitle ?: "Servicio contratado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Monto acordado y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = currencyFormat.format(contract.agreedAmount),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = formatMeetingDate(contract.meetingDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Botones de acción según el estado
            when (contract.currentStatus) {
                "CREATED", "MEETING_SCHEDULED" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onOpenChatClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Chat", fontSize = 12.sp)
                        }

                        Button(
                            onClick = onGenerateCodeClick,
                            enabled = !isGeneratingCode,
                            modifier = Modifier.weight(1.5f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009963))
                        ) {
                            if (isGeneratingCode) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Outlined.Pin, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generar PIN", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                "VERIFIED" -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                            Text(
                                text = "Encuentro presencial verificado. El trabajador está preparando la factura final con la mano de obra acordada.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
                "INVOICED" -> {
                    val laborCost = contract.agreedAmount
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFFFB74D))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ReceiptLong,
                                        contentDescription = null,
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Factura de Mano de Obra: ${currencyFormat.format(laborCost)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFE65100)
                                    )
                                }
                                Text(
                                    text = "El trabajador emitió la cuenta de cobro. Tu pago quedará protegido en custodia (escrow) y solo se liberará al calificar el trabajo completado.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF8D6E63)
                                )
                            }
                        }

                        Button(
                            onClick = onPayLaborClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                        ) {
                            Icon(imageVector = Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pagar Mano de Obra", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                "ACTIVE" -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFE0F2F1),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF00695C), modifier = Modifier.size(18.dp))
                            Text(
                                text = "Pago en garantía (Escrow) confirmado. El trabajador está realizando las labores.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF00695C)
                            )
                        }
                    }
                }
                "COMPLETED_BY_WORKER" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFEDE7F6),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🎉 ¡El trabajador marcó el trabajo como terminado! Por favor califica su atención para finalizar el contrato.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF512DA8),
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Button(
                            onClick = onReviewClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                        ) {
                            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Calificar Servicio", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                "COMPLETED" -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "✅ Contrato completado y calificado satisfactoriamente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2E7D32),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientStatusChip(status: String) {
    val (label, bg, text) = when (status) {
        "CREATED" -> Triple("Formalizado", Color(0xFFFFF8E1), Color(0xFFF57F17))
        "MEETING_SCHEDULED" -> Triple("Cita Programada", Color(0xFFE3F2FD), Color(0xFF1565C0))
        "VERIFIED" -> Triple("Verificado", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "INVOICED" -> Triple("Pago Pendiente", Color(0xFFFFF3E0), Color(0xFFE65100))
        "ACTIVE" -> Triple("En Ejecución", Color(0xFFE0F2F1), Color(0xFF00695C))
        "COMPLETED_BY_WORKER" -> Triple("Por Calificar", Color(0xFFEDE7F6), Color(0xFF512DA8))
        "COMPLETED" -> Triple("Completado", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "DISPUTED" -> Triple("En Disputa", Color(0xFFFFEBEE), Color(0xFFC62828))
        else -> Triple(status, Color.LightGray.copy(alpha = 0.3f), Color.DarkGray)
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = label,
            color = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun VerificationCodeDisplayDialog(
    contract: ContractRes,
    code: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color(0xFF009963), modifier = Modifier.size(32.dp))
                    }
                }

                Text(
                    text = "Código de Verificación Presencial",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Muéstrale o díctale este código al trabajador cuando llegue a tu ubicación para confirmar su asistencia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                // Código en grande espaciado
                val formattedCode = if (code.length == 6) "${code.substring(0, 3)} ${code.substring(3)}" else code
                Surface(
                    color = Color(0xFFF1F8F5),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF009963).copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(
                        text = formattedCode,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF009963),
                        textAlign = TextAlign.Center,
                        letterSpacing = 4.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            copied = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (copied) "¡Copiado!" else "Copiar", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009963))
                    ) {
                        Text("Cerrar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "⏰ Válido por 30 minutos desde su generación",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EmptyContractsView(
    filter: String,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Text(
            text = "No tienes contratos en esta sección",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Cuando contrates a un trabajador tras pagar la tarifa de seguridad, tus contratos aparecerán aquí.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        OutlinedButton(
            onClick = onRefresh,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Refrescar")
        }
    }
}

private fun formatMeetingDate(dateStr: String?): String {
    if (dateStr.isNullOrBlank()) return "Por acordar en chat"
    return try {
        val parsed = java.time.LocalDate.parse(dateStr)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", Locale("es", "CO"))
        parsed.format(formatter)
    } catch (e: Exception) {
        dateStr
    }
}

@Composable
private fun LaborPaymentDialog(
    contract: ContractRes,
    isPaying: Boolean,
    onConfirm: (method: String) -> Unit,
    onDismiss: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
    }
    val laborCost = contract.agreedAmount

    Dialog(onDismissRequest = { if (!isPaying) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFF3E0),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Text(
                    text = "Pago de Mano de Obra",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "El trabajador completó el diagnóstico o visita y generó la factura final por la labor a realizar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Surface(
                    color = Color(0xFFF9FBE7),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFC0CA33)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Monto a Pagar",
                            fontSize = 12.sp,
                            color = Color(0xFF827717)
                        )
                        Text(
                            text = currencyFormat.format(laborCost),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF33691E)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Fondos protegidos en Escrow (Garantía)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Text(
                    text = "🛡️ Este dinero NO se transfiere directamente al trabajador todavía. Nexum retiene el pago en custodia de garantía y solo lo libera cuando confirmes que el servicio fue realizado a satisfacción.\n\nAl continuar, se abrirá la pasarela segura de Mercado Pago para procesar tu transacción.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !isPaying,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancelar", fontSize = 13.sp)
                    }

                    Button(
                        onClick = { onConfirm("credit_card") },
                        enabled = !isPaying,
                        modifier = Modifier.weight(1.8f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009EE3))
                    ) {
                        if (isPaying) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payment,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text("Pagar con Mercado Pago", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}


