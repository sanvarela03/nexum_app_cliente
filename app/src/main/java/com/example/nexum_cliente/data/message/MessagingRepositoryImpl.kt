package com.example.nexum_cliente.data.message

import android.util.Log
import com.example.nexum_cliente.data.message.remote.api.MessagingApiService
import com.example.nexum_cliente.data.message.remote.payload.req.MessageRequest
import com.example.nexum_cliente.data.message.remote.websocket.WebSocketManager
import com.example.nexum_cliente.domain.model.ConnectionState
import com.example.nexum_cliente.domain.model.Conversation
import com.example.nexum_cliente.domain.model.Message
import com.example.nexum_cliente.domain.model.PageResponse
import com.example.nexum_cliente.domain.repository.MessagingRepository
import com.example.nexum_cliente.security.TokenManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingRepositoryImpl @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val apiService: MessagingApiService,
    private val tokenManager: TokenManager
) : MessagingRepository {

    // WebSocket
    override val connectionState: StateFlow<ConnectionState> =
        webSocketManager.connectionState

    override val incomingMessages: StateFlow<Message?> =
        webSocketManager.messages

    override suspend fun connectWebSocket(serverUrl: String, jwtToken: String) {
        webSocketManager.connect(serverUrl, jwtToken)
    }

    override suspend fun disconnectWebSocket() {
        webSocketManager.disconnect()
    }

    override suspend fun subscribeToMessages() {
        webSocketManager.subscribeToMessages()
    }

    override suspend fun sendMessageViaWebSocket(message: MessageRequest) {
        webSocketManager.sendMessage(message)
    }

    override suspend fun markAsReadViaWebSocket(conversationId: String) {
        webSocketManager.markAsRead(conversationId)
    }

    override suspend fun notifyTyping(conversationId: String, isTyping: Boolean) {
        webSocketManager.notifyTyping(conversationId, isTyping)
    }

    // REST API
    override suspend fun sendMessageViaRest(message: MessageRequest): Result<Message> {
        return try {
            val token = tokenManager.getAccessToken().firstOrNull()
                ?: return Result.failure(Exception("No token found"))

            val response = apiService.sendMessage(token, message)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversations(
        page: Int,
        size: Int
    ): Result<PageResponse<Conversation>> {
        return try {
            val token = tokenManager.getAccessToken().firstOrNull()
                ?: return Result.failure(Exception("No token found"))

            Log.d("MessagingRepositoryImpl", "token: $token")
            val response = apiService.getConversations(token, page, size)
            Log.d("MessagingRepositoryImpl", "Response: $response")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversationMessages(
        conversationId: String,
        page: Int,
        size: Int
    ): Result<PageResponse<Message>> {
        return try {
            val token = tokenManager.getAccessToken().firstOrNull()
                ?: return Result.failure(Exception("No token found"))

            val response = apiService.getConversationMessages(
                token, conversationId, page, size
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsReadViaRest(conversationId: String): Result<Unit> {
        return try {
            val token = tokenManager.getAccessToken().firstOrNull()
                ?: return Result.failure(Exception("No token found"))

            apiService.markAsRead(token, conversationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadCount(): Result<Long> {
        return try {
            val token = tokenManager.getAccessToken().firstOrNull()
                ?: return Result.failure(Exception("No token found"))

            val response = apiService.getUnreadCount(token)
            Result.success(response.unreadCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}