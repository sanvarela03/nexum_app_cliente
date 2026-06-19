package com.example.nexum_cliente.ui.components.creditcard

data class CreditCardState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val cardType: CardType = CardType.UNKNOWN
)

enum class CardType {
    VISA,
    MASTERCARD,
    AMEX,
    UNKNOWN
}
