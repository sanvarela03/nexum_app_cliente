package com.example.neuxum_cliente.ui.navigation.graphs

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
sealed class Graph {
    @Serializable
    data object InitialGraph : Graph()
}