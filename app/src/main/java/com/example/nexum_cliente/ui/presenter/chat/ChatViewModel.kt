package com.example.nexum_cliente.ui.presenter.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.message.remote.payload.req.MessageRequest
import com.example.nexum_cliente.di.modules.WebSocketUrl
import com.example.nexum_cliente.domain.model.ConnectionState
import com.example.nexum_cliente.domain.model.Message
import com.example.nexum_cliente.domain.model.MessageType
import com.example.nexum_cliente.domain.repository.MessagingRepository
import com.example.nexum_cliente.security.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.6
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: MessagingRepository,
    private val tokenManager: TokenManager,
    @WebSocketUrl private val wsUrl: String
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val connectionState: SharedFlow<ConnectionState> = repository.connectionState

    private var currentConversationId: String? = null
    private var currentPage = 0
    private val pageSize = 50
    private var canLoadMore = true

    init {
        observeIncomingMessages()
    }

    fun connectWebSocket() {
        viewModelScope.launch {
            tokenManager.getAccessToken().firstOrNull()?.let { token ->
                repository.connectWebSocket(wsUrl, token)
            }
        }
    }

    fun disconnectWebSocket() {
        viewModelScope.launch {
            repository.disconnectWebSocket()
        }
    }

    fun loadConversationMessages(conversationId: String, loadMore: Boolean = false) {
        if (!loadMore) {
            currentConversationId = conversationId
            currentPage = 0
            canLoadMore = true
            _messages.value = emptyList()
        }

        if (!canLoadMore) return

        viewModelScope.launch {
            _uiState.value = if (loadMore) ChatUiState.LoadingMore else ChatUiState.Loading

            repository.getConversationMessages(conversationId, currentPage, pageSize)
                .onSuccess { response ->
                    val newMessages = if (loadMore) {
                        _messages.value + response.content
                    } else {
                        response.content
                    }
                    _messages.value = newMessages
                    currentPage++
                    canLoadMore = response.content.size == pageSize
                    _uiState.value = ChatUiState.Success

                    // Marcar como leído al cargar los mensajes iniciales
                    if (!loadMore) {
                        markAsRead()
                    }
                }
                .onFailure { error ->
                    Log.e("ChatViewModel", "Error loading messages", error)
                    _uiState.value = ChatUiState.Error(
                        error.message ?: "Error loading messages"
                    )
                }
        }
    }

    fun sendMessage(
        receiverId: String,
        receiverRole: String,
        content: String,
        type: MessageType = MessageType.TEXT,
        metadata: Map<String, String>? = null,
        replyToMessageId: String? = null
    ) {
        viewModelScope.launch {
            val request = MessageRequest(
                receiverId = receiverId,
                receiverRole = receiverRole,
                type = type,
                content = content,
                metadata = metadata,
                replyToMessageId = replyToMessageId
            )
            repository.sendMessageViaWebSocket(request)
        }
    }

    fun markAsRead() {
        currentConversationId?.let { conversationId ->
            viewModelScope.launch {
                repository.markAsReadViaWebSocket(conversationId)
            }
        }
    }

    fun notifyTyping(isTyping: Boolean) {
        currentConversationId?.let { conversationId ->
            viewModelScope.launch {
                repository.notifyTyping(conversationId, isTyping)
            }
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            repository.incomingMessages.collect { message ->
                message?.let { msg ->
                    val activeId = currentConversationId
                    if (activeId != null && msg.conversationId == activeId) {
                        addMessageToList(msg)
                        markAsRead()
                    }
                }
            }
        }
    }

    private fun addMessageToList(message: Message) {
        val currentList = _messages.value.toMutableList()
        if (!currentList.any { it.id == message.id }) {
            currentList.add(0, message)
            _messages.value = currentList
        }
    }

    fun clearError() {
        _uiState.value = ChatUiState.Success
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }
}

sealed class ChatUiState {
    object Loading : ChatUiState()
    object LoadingMore : ChatUiState()
    object Success : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}
