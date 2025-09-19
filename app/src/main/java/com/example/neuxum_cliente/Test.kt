package com.example.neuxum_cliente

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */

suspend fun fetchData(): String {
    delay(1000)
    return "[1, 2]"
}

data class Pedido(val nombre: String, val tiempoPreparacion: Int)

/** Producer scope (keeps running regardless of collectors) */
private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

/** Hot stream: holds the latest value and replays exactly one (the current one) */
private val _flujoDeNumeros = MutableStateFlow(0) // initial state
val flujoDeNumeros: StateFlow<Int> = _flujoDeNumeros.asStateFlow()

private fun startProducing() = appScope.launch {
    // Emit 1..60 every second
    repeat(60) { idx ->
        val next = idx + 1
        _flujoDeNumeros.value = next
        delay(1000)
    }
}

val mainFlow = flowOf(1,2)

fun main() = runBlocking {
    flow {
        emit(1)
        delay(50)
        emit(2)
    }.withIndex().collect { value ->
        println("Collecting ${value.index} ${value.value}")
        delay(100) // Emulate work
        println("$value collected")
    }
}

suspend fun prepararPedido(pedido: Pedido) {
    println("Preparando ${pedido.nombre} (tiempo estimado: ${pedido.tiempoPreparacion} segundos)...")
    delay(pedido.tiempoPreparacion * 1000L) // Simula el tiempo de preparación
    println("${pedido.nombre} esta listo.")
}
