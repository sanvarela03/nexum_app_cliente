package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
    viewModel: MercadoPagoViewModel = hiltViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    ),
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state

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

    // Dialog resultado del pago
    state.paymentResult?.let { result ->
        AlertDialog(
            onDismissRequest = { viewModel.resetPaymentResult() },
            confirmButton = {
                TextButton(onClick = { viewModel.resetPaymentResult() }) {
                    Text("OK")
                }
            },
            title = {
                Text(
                    when (result) {
                        is PaymentResult.Success -> "✅ Pago exitoso"
                        is PaymentResult.Failure -> "❌ Pago fallido"
                        is PaymentResult.Pending -> "⏳ Pago pendiente"
                    }
                )
            },
            text = {
                if (result is PaymentResult.Success) {
                    Text("ID de pago: ${result.paymentId}")
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
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Cancelar",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = { viewModel.createPreference() },
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(4.dp), // Casi rectangular
                modifier = Modifier
                    .weight(1.2f)
                    .height(56.dp)
            ) {
                if (state.isLoading) {
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
