package com.example.nexum_cliente.common

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/30/2025
 * @version 1.0
 */
// Local IP address for the Android Emulator to connect to the host machine's localhost
//private val IP = "10.0.2.2"

private val IP = "192.168.1.45"
private val PORT = "8080"
private val MSG_PORT = "8081"

val HOST_URL = "http://$IP:$PORT"
val MSG_URL = "http://$IP:$MSG_PORT"
val BASE_URL = MSG_URL
val WS_URL = "ws://$IP:$MSG_PORT"

const val APP_KEY = "a3f2c1b1-8e4b-4a6d-8b9e-0c1f2a3b4d5e"