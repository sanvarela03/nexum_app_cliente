package com.example.nexum_cliente.ui.navigation.rutes

import kotlinx.serialization.Serializable

@Serializable
sealed class PlaygroundRoutes {
    @Serializable
    object CreditCardForm : PlaygroundRoutes()
}
