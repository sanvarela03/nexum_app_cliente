package com.example.nexum_cliente.ui.presenter.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nexum_cliente.ui.components.creditcard.CreditCardForm
import com.example.nexum_cliente.ui.components.creditcard.CreditCardState
import kotlinx.coroutines.launch

/**
 * Example screen demonstrating how to embed [CreditCardForm] inside a
 * real Compose screen with a [Scaffold], back navigation, and a Snackbar
 * confirmation.
 *
 * This is an integration example. In production, replace [rememberSaveable]
 * state with a ViewModel that handles payment submission.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onNavigateBack: () -> Unit = {}
) {
    var cardState by rememberSaveable { mutableStateOf(CreditCardState()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val titlePayment = "Payment"
    val titleCardDetails = "Card details"
    val messageSuccess = "Payment submitted successfully"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titlePayment) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = titleCardDetails,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            CreditCardForm(
                state = cardState,
                onStateChange = { cardState = it },
                onSubmit = { submittedState ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(messageSuccess)
                    }
                }
            )
        }
    }
}
