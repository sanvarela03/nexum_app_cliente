package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MercadoPagoCheckout(
    viewModel: MercadoPagoViewModel = hiltViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Nueva orden de pago", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = state.title,
            onValueChange = { viewModel.onEvent(MercadoPagoEvent.TitleChanged(it)) },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.price,
            onValueChange = { viewModel.onEvent(MercadoPagoEvent.PriceChanged(it)) },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = state.quantity,
            onValueChange = { viewModel.onEvent(MercadoPagoEvent.QuantityChanged(it)) },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { viewModel.createPreference() },
            enabled = state.isValid() && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Pagar")
            }
        }
    }
}
