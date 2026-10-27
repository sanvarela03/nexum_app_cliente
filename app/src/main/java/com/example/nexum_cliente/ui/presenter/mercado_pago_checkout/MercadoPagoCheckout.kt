package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_cliente.ui.components.Stepper

@Composable
fun MercadoPagoCheckout(
    jobApplicationId: Long? = null,
    agreedAmount: Double? = null,
    viewModel: MercadoPagoViewModel = hiltViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    ),
    onCancel: () -> Unit = {},
    onContractCreated: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val state = viewModel.state

    // Interceptar Back del sistema si ya se pagó o existe contrato para no volver a la pantalla previa
    BackHandler(enabled = state.isPaymentOrContractDone) {
        onContractCreated(state.createdContract?.id ?: -1L)
    }

    LaunchedEffect(jobApplicationId, agreedAmount) {
        if (jobApplicationId != null && agreedAmount != null) {
            viewModel.setCheckoutInfo(jobApplicationId, agreedAmount)
        }
    }

    // Abrir Custom Tab cuando llega el initPoint
    LaunchedEffect(state.initPoint) {
        state.initPoint?.let { url ->
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
                .launchUrl(context, Uri.parse(url))
            viewModel.resetInitPoint()
        }
    }

    // Dialog resultado del pago y creación de contrato
    state.paymentResult?.let { result ->
        AlertDialog(
            onDismissRequest = {
                val contractId = state.createdContract?.id
                viewModel.resetPaymentResult()
                if (contractId != null) {
                    onContractCreated(contractId)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val contractId = state.createdContract?.id
                    viewModel.resetPaymentResult()
                    if (contractId != null) {
                        onContractCreated(contractId)
                    } else if (result is PaymentResult.Success && !state.isCreatingContract) {
                        viewModel.createContract()
                    }
                }) {
                    Text(
                        if (state.createdContract != null) "Ver Seguimiento"
                        else if (result is PaymentResult.Success && state.isCreatingContract) "Creando contrato..."
                        else if (result is PaymentResult.Success) "Formalizar Contrato"
                        else "OK"
                    )
                }
            },
            title = {
                Text(
                    when {
                        state.createdContract != null -> "🎉 ¡Contrato formalizado!"
                        result is PaymentResult.Success -> "✅ Pago exitoso"
                        result is PaymentResult.Failure -> "❌ Pago fallido"
                        result is PaymentResult.Pending -> "⏳ Pago pendiente"
                        else -> "Transacción"
                    }
                )
            },
            text = {
                Column {
                    if (result is PaymentResult.Success) {
                        Text("ID de pago: ${result.paymentId}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (state.isCreatingContract) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generando contrato oficial...")
                        }
                    } else if (state.createdContract != null) {
                        Text("Tu propuesta fue aceptada y el contrato #${state.createdContract?.id} ha sido creado.\n\nYa puedes coordinar la fecha de encuentro y generar el código de verificación presencial.")
                    } else if (result is PaymentResult.Success) {
                        Text("La tarifa fue procesada. Puedes formalizar el contrato ahora.")
                    } else if (result is PaymentResult.Failure) {
                        Text("No se pudo completar el pago de la tarifa de seguridad y confianza.")
                    } else if (result is PaymentResult.Pending) {
                        Text("El pago está pendiente de confirmación.")
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stepper
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 16.dp)
        ) {
            Stepper(
                numberOfSteps = 4,
                currentStep = 2,
                selectedColor = Color(0xFF00A650), // Color verde aproximado al de MercadoPago/imagen
                unSelectedColor = Color(0xFFE0E0E0)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Título Principal
        Text(
            text = "Tarifa de\nseguridad y\nconfianza",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtítulo
        Text(
            text = "Importe que asegura el cumplimiento de esta transacción y los intereses de Nexum.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Total
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total: ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Text(
                text = "$ 8 700 COP",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        if (state.isContractEstablished) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE8F5E9),
                border = BorderStroke(1.dp, Color(0xFF00A650).copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00A650),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Propuesta Ya Contratada",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = state.createdContract?.id?.let { "Esta propuesta ya cuenta con el contrato formalizado #$it. No se requiere ningún pago adicional." }
                                ?: "Esta propuesta ya ha sido formalizada en un contrato. No se requiere ningún pago adicional.",
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Disclaimer
        Text(
            text = "Esta tarifa será devuelta cuando el trabajador y el cliente lleguen a un acuerdo de forma presencial.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Error si hay
        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botones Bottom
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    if (state.isPaymentOrContractDone) {
                        onContractCreated(state.createdContract?.id ?: -1L)
                    } else {
                        onCancel()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (state.isPaymentOrContractDone) "Volver" else "Cancelar",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            if (state.isContractEstablished) {
                Button(
                    onClick = { onContractCreated(state.createdContract?.id ?: -1L) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A650)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Ver Contrato",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else if (state.isPaymentSuccess && state.isCreatingContract) {
                Button(
                    onClick = { },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(56.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Formalizando...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else if (state.isPaymentSuccess && state.createdContract == null) {
                Button(
                    onClick = { viewModel.createContract() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A650)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Formalizar Contrato",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.createPreference() },
                    enabled = !state.isLoading && !state.isCheckingExistingContract,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        disabledContainerColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(56.dp)
                ) {
                    if (state.isLoading || state.isCheckingExistingContract) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Proceder al pago",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
