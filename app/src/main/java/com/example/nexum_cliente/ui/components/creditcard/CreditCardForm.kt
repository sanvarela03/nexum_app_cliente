package com.example.nexum_cliente.ui.components.creditcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// Validation helpers
// ---------------------------------------------------------------------------

private fun isCardNumberValid(raw: String): Boolean = raw.length == 16
private fun isCardHolderNameValid(name: String): Boolean = name.isNotBlank()
private fun isExpirationDateValid(raw: String): Boolean = raw.length == 4
private fun isCvvValid(raw: String): Boolean = raw.length in 3..4

private fun isFormValid(state: CreditCardState): Boolean =
    isCardNumberValid(state.cardNumber) &&
        isCardHolderNameValid(state.cardHolderName) &&
        isExpirationDateValid(state.expirationDate) &&
        isCvvValid(state.cvv)

// ---------------------------------------------------------------------------
// Card type label
// ---------------------------------------------------------------------------

internal fun CardType.label(): String = when (this) {
    CardType.VISA -> "Visa"
    CardType.MASTERCARD -> "Mastercard"
    CardType.AMEX -> "Amex"
    CardType.UNKNOWN -> ""
}

// ---------------------------------------------------------------------------
// Main composable
// ---------------------------------------------------------------------------

/**
 * A reusable, self-contained credit card form built with Material 3.
 *
 * @param state         Current form state (hoisted).
 * @param onStateChange Called whenever any field value changes.
 * @param modifier      Optional [Modifier] applied to the root [Column].
 * @param onSubmit      Called with the current [CreditCardState] when the
 *                      submit button is tapped and all fields are valid.
 */
@Composable
fun CreditCardForm(
    state: CreditCardState,
    onStateChange: (CreditCardState) -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: (CreditCardState) -> Unit = {}
) {
    // Track which fields have been touched so we only show errors after interaction
    var cardNumberDirty by rememberSaveable { mutableStateOf(false) }
    var cardHolderNameDirty by rememberSaveable { mutableStateOf(false) }
    var expirationDateDirty by rememberSaveable { mutableStateOf(false) }
    var cvvDirty by rememberSaveable { mutableStateOf(false) }

    val labelCardNumber = "Card Number"
    val labelCardHolder = "Cardholder Name"
    val labelExpirationDate = "Expiry (MM/YY)"
    val labelCvv = "CVV"
    val labelSubmit = "Submit"
    val errorCardNumber = "Enter a valid 16-digit card number"
    val errorCardHolder = "Cardholder name is required"
    val errorExpirationDate = "Enter a valid expiry date (MM/YY)"
    val errorCvv = "Enter a valid CVV (3–4 digits)"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── Card Number ──────────────────────────────────────────────────────
        val cardNumberError = cardNumberDirty && !isCardNumberValid(state.cardNumber)
        OutlinedTextField(
            value = state.cardNumber,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }.take(16)
                cardNumberDirty = true
                onStateChange(
                    state.copy(
                        cardNumber = digits,
                        cardType = detectCardType(digits)
                    )
                )
            },
            label = { Text(labelCardNumber) },
            trailingIcon = {
                val typeLabel = state.cardType.label()
                if (typeLabel.isNotEmpty()) {
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            },
            isError = cardNumberError,
            supportingText = {
                if (cardNumberError) {
                    Text(
                        text = errorCardNumber,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = CardNumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // ── Cardholder Name ──────────────────────────────────────────────────
        val cardHolderError = cardHolderNameDirty && !isCardHolderNameValid(state.cardHolderName)
        OutlinedTextField(
            value = state.cardHolderName,
            onValueChange = { input ->
                cardHolderNameDirty = true
                onStateChange(state.copy(cardHolderName = input.take(26)))
            },
            label = { Text(labelCardHolder) },
            isError = cardHolderError,
            supportingText = {
                if (cardHolderError) {
                    Text(
                        text = errorCardHolder,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // ── Expiry + CVV side by side ────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Expiration date
            val expiryError = expirationDateDirty && !isExpirationDateValid(state.expirationDate)
            OutlinedTextField(
                value = state.expirationDate,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(4)
                    expirationDateDirty = true
                    onStateChange(state.copy(expirationDate = digits))
                },
                label = { Text(labelExpirationDate) },
                isError = expiryError,
                supportingText = {
                    if (expiryError) {
                        Text(
                            text = errorExpirationDate,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = ExpirationDateVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            // CVV
            val cvvError = cvvDirty && !isCvvValid(state.cvv)
            OutlinedTextField(
                value = state.cvv,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(4)
                    cvvDirty = true
                    onStateChange(state.copy(cvv = digits))
                },
                label = { Text(labelCvv) },
                isError = cvvError,
                supportingText = {
                    if (cvvError) {
                        Text(
                            text = errorCvv,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = CvvVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Submit button ────────────────────────────────────────────────────
        Button(
            onClick = { onSubmit(state) },
            enabled = isFormValid(state),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(labelSubmit)
        }
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun CreditCardFormPreview() {
    var state by remember { mutableStateOf(CreditCardState()) }
    MaterialTheme {
        CreditCardForm(
            state = state,
            onStateChange = { state = it },
            onSubmit = {}
        )
    }
}
