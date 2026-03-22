package com.example.nexum_cliente

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/26/2026
 * @version 1.0
 */
interface Volar {
    fun volar(): Unit
}

interface Nadar {
    fun nadar(): Unit
}


class Perro(
   val firulais: Nadar
) : Volar, Nadar by firulais {
    override fun volar() {
        println("El perro está volando.")
    }
}


var perro = Perro(
    firulais = object : Nadar {
        override fun nadar() {
            println("El perro está nadando.")
        }
    }
)


fun main() {
    perro.firulais
    perro.volar()
}

