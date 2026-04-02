package com.example.nexum_cliente.common

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/30/2025
 * @version 1.0
 */
// Local IP address for the Android Emulator to connect to the host machine's localhost
private val IP = "10.0.2.2"

//private val IP = "192.168.1.45"
private val PORT = "8080"
private val MSG_PORT = "8081"

private val LOCAL_URL = "http://$IP:$PORT"
private val LOCAL_MSG_URL = "http://$IP:$MSG_PORT"
private val LOCAL_WS_URL = "ws://$IP:$MSG_PORT"

private val REMOTE_URL = "https://nexum-be.onrender.com"
private val REMOTE_MSG_URL = "https://nexum-msg-be.onrender.com"
private val REMOTE_WS_URL = "wss://nexum-msg-be.onrender.com"


val useLocal = false

val HOST_URL = if (useLocal) LOCAL_URL else REMOTE_URL
val MSG_URL = if (useLocal) LOCAL_MSG_URL else REMOTE_MSG_URL
val BASE_URL = MSG_URL
val WS_URL = if (useLocal) LOCAL_WS_URL else REMOTE_WS_URL

const val APP_KEY = "a3f2c1b1-8e4b-4a6d-8b9e-0c1f2a3b4d5e"