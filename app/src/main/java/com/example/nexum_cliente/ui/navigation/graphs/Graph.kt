package com.example.nexum_cliente.ui.navigation.graphs

import kotlinx.serialization.Serializable

@Serializable
sealed interface Graph {
    @Serializable
    object Auth : Graph

    @Serializable
    object Home : Graph
}
