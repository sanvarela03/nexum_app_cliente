package com.example.nexum_cliente.ui.components.creditcard

/**
 * Detects the card network type from a raw card number string.
 * Safe to call with empty or partial input.
 */
fun detectCardType(number: String): CardType {
    if (number.isEmpty()) return CardType.UNKNOWN

    return when {
        number.startsWith("4") -> CardType.VISA
        number.length >= 2 && number.substring(0, 2).toIntOrNull()
            ?.let { it in 51..55 } == true -> CardType.MASTERCARD
        number.startsWith("34") || number.startsWith("37") -> CardType.AMEX
        else -> CardType.UNKNOWN
    }
}
