package com.example.nexum_cliente.data.message.remote.websocket

import android.util.Log
import com.example.nexum_cliente.data.message.remote.payload.req.MessageRequest
import com.example.nexum_cliente.domain.model.ConnectionState
import com.example.nexum_cliente.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
@Singleton
class WebSocketManager @Inject constructor(
    private val httpClient: HttpClient,
    private val json: Json
) {
    companion object {
        private const val TAG = "WebSocketManager"
    }

    private var webSocketSession: DefaultClientWebSocketSession? = null
    private var connectionJob: Job? = null
    private val subscriptionId = AtomicInteger(0)

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _messages = MutableStateFlow<Message?>(null)
    val messages: StateFlow<Message?> = _messages.asStateFlow()

    suspend fun connect(serverUrl: String, jwtToken: String) {
        if (_connectionState.value == ConnectionState.CONNECTED) {
            Log.d(TAG, "Already connected")
            return
        }

        _connectionState.value = ConnectionState.CONNECTING

        connectionJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                httpClient.webSocket(urlString = "$serverUrl/ws/websocket") {
                    webSocketSession = this
                    Log.d(TAG, "✅ WebSocket connected")

                    sendStompConnect(jwtToken)

                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    handleIncomingFrame(frame.readText())
                                }

                                is Frame.Close -> {
                                    Log.d(TAG, "🔌 Connection closed")
                                    _connectionState.value = ConnectionState.DISCONNECTED
                                }

                                else -> {}
                            }
                        }
                    } catch (e: ClosedReceiveChannelException) {
                        Log.d(TAG, "Channel closed")
                        _connectionState.value = ConnectionState.DISCONNECTED
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Error: ${e.message}", e)
                        _connectionState.value = ConnectionState.ERROR
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Connection error: ${e.message}", e)
                _connectionState.value = ConnectionState.ERROR
            }
        }
    }

    private suspend fun sendStompConnect(jwtToken: String) {
        val connectFrame = StompFrame(
            command = "CONNECT",
            headers = mapOf(
                "Authorization" to "Bearer $jwtToken",
                "accept-version" to "1.2",
                "heart-beat" to "10000,10000"
            )
        )
        webSocketSession?.send(Frame.Text(connectFrame.toRawString()))
        Log.d(TAG, "📤 CONNECT sent")
    }

    suspend fun subscribeToMessages() {
        val subId = subscriptionId.incrementAndGet().toString()
        val subscribeFrame = StompFrame(
            command = "SUBSCRIBE",
            headers = mapOf(
                "id" to subId,
                "destination" to "/user/queue/messages"
            )
        )
        webSocketSession?.send(Frame.Text(subscribeFrame.toRawString()))
        Log.d(TAG, "📥 Subscribed to messages with ID: $subId")
    }

    suspend fun sendMessage(message: MessageRequest) {
        try {
            val messageJson = json.encodeToString(MessageRequest.serializer(), message)
            val sendFrame = StompFrame(
                command = "SEND",
                headers = mapOf(
                    "destination" to "/app/chat.send",
                    "content-type" to "application/json"
                ),
                body = messageJson
            )
            webSocketSession?.send(Frame.Text(sendFrame.toRawString()))
            Log.d(TAG, "📤 Message sent")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error sending message: ${e.message}", e)
        }
    }

    suspend fun markAsRead(conversationId: String) {
        val payload = """{"conversationId":"$conversationId"}"""
        val sendFrame = StompFrame(
            command = "SEND",
            headers = mapOf(
                "destination" to "/app/chat.read",
                "content-type" to "application/json"
            ),
            body = payload
        )
        webSocketSession?.send(Frame.Text(sendFrame.toRawString()))
        Log.d(TAG, "✅ Marked as read: $conversationId")
    }

    suspend fun notifyTyping(conversationId: String, isTyping: Boolean) {
        val payload = """{"conversationId":"$conversationId","isTyping":$isTyping}"""
        val sendFrame = StompFrame(
            command = "SEND",
            headers = mapOf(
                "destination" to "/app/chat.typing",
                "content-type" to "application/json"
            ),
            body = payload
        )
        webSocketSession?.send(Frame.Text(sendFrame.toRawString()))
    }

    private suspend fun confirmDelivery(messageId: String) {
        val payload = """{"messageId":"$messageId"}"""
        val sendFrame = StompFrame(
            command = "SEND",
            headers = mapOf(
                "destination" to "/app/chat.delivered",
                "content-type" to "application/json"
            ),
            body = payload
        )
        webSocketSession?.send(Frame.Text(sendFrame.toRawString()))
    }

    private fun handleIncomingFrame(rawFrame: String) {
        val frame = StompFrame.fromRawString(rawFrame) ?: return

        when (frame.command) {
            "CONNECTED" -> {
                Log.d(TAG, "✅ STOMP connected")
                _connectionState.value = ConnectionState.CONNECTED
            }

            "MESSAGE" -> {
                try {
                    val message = json.decodeFromString<Message>(frame.body)
                    Log.d(TAG, "📨 Message received: ${message.id}")
                    _messages.value = message

                    // Auto-confirm delivery
                    CoroutineScope(Dispatchers.IO).launch {
                        confirmDelivery(message.id)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing message: ${e.message}", e)
                }
            }

            "ERROR" -> {
                Log.e(TAG, "❌ STOMP error: ${frame.body}")
                _connectionState.value = ConnectionState.ERROR
            }

            "RECEIPT" -> {
                Log.d(TAG, "📬 Receipt: ${frame.headers}")
            }

            else -> {
                Log.d(TAG, "Frame received: ${frame.command}")
            }
        }
    }

    suspend fun disconnect() {
        try {
            val disconnectFrame = StompFrame(command = "DISCONNECT")
            webSocketSession?.send(Frame.Text(disconnectFrame.toRawString()))
            delay(100) // Give time to send
            webSocketSession?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting: ${e.message}")
        } finally {
            connectionJob?.cancel()
            _connectionState.value = ConnectionState.DISCONNECTED
            Log.d(TAG, "🔌 Disconnected")
        }
    }
}