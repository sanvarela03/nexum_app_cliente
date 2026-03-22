package com.example.nexum_cliente.ui.presenter.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.message.remote.payload.req.MessageRequest
import com.example.nexum_cliente.data.message.remote.websocket.WebSocketEvent
import com.example.nexum_cliente.di.modules.WebSocketUrl
import com.example.nexum_cliente.domain.model.ConnectionState
import com.example.nexum_cliente.domain.model.Message
import com.example.nexum_cliente.domain.model.MessageStatus
import com.example.nexum_cliente.domain.model.MessageType
import com.example.nexum_cliente.domain.repository.MessagingRepository
import com.example.nexum_cliente.domain.use_cases.profile.ProfileUseCases
import com.example.nexum_cliente.security.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.9
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: MessagingRepository,
    private val tokenManager: TokenManager,
    private val profileUseCases: ProfileUseCases,
    @WebSocketUrl private val wsUrl: String
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val connectionState: SharedFlow<ConnectionState> = repository.connectionState

    val profiles = profileUseCases.observeProfile()

    private val _isReceiverTyping = MutableStateFlow(false)
    val isReceiverTyping: StateFlow<Boolean> = _isReceiverTyping.asStateFlow()

    private var currentConversationId = MutableStateFlow<String?>(null)
    private var targetReceiverId: String? = null
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

    fun loadConversationMessages(conversationId: String, receiverId: String? = null, loadMore: Boolean = false) {
        if (!loadMore) {
            currentConversationId.value = conversationId
            targetReceiverId = receiverId
            currentPage = 0
            canLoadMore = true
            _messages.value = emptyList()
            
            if (conversationId == "new") {
                _uiState.value = ChatUiState.Success
                return
            }
        }

        if (!canLoadMore) return

        viewModelScope.launch {
            _uiState.value = if (loadMore) ChatUiState.LoadingMore else ChatUiState.Loading

            repository.getConversationMessages(conversationId, currentPage, pageSize)
                .onSuccess { response ->
                    val newMessages = if (loadMore) {
                        (_messages.value + response.content).distinctBy { it.id }
                    } else {
                        response.content.distinctBy { it.id }
                    }
                    _messages.value = newMessages
                    currentPage++
                    canLoadMore = response.content.size == pageSize
                    _uiState.value = ChatUiState.Success

                    if (!loadMore) { markAsRead() }
                }
                .onFailure { error ->
                    Log.e("ChatViewModel", "Error loading messages", error)
                    _uiState.value = ChatUiState.Error(error.message ?: "Error loading messages")
                }
        }
    }

    fun fetchReceiverProfile(receiverId: String) {
        val userId = receiverId.toLongOrNull() ?: return
        viewModelScope.launch {
            profileUseCases.updateProfile(listOf(userId), fetchFromRemote = true).collect()
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
            // BUG FIX: Esperar a que el WebSocket esté realmente conectado
            if (repository.connectionState.first() != ConnectionState.CONNECTED) {
                connectWebSocket()
                // Esperar un máximo de 3 segundos a que conecte
                repository.connectionState.filter { it == ConnectionState.CONNECTED }.take(1).collect()
            }

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
        val id = currentConversationId.value
        if (id != null && id != "new") {
            viewModelScope.launch { repository.markAsReadViaWebSocket(id) }
        }
    }

    private var typingJob: Job? = null
    private var isCurrentlyTyping = false

    fun setTyping() {
        val id = currentConversationId.value
        if (id == null || id == "new") return

        typingJob?.cancel()

        if (!isCurrentlyTyping) {
            isCurrentlyTyping = true
            viewModelScope.launch {
                repository.notifyTyping(id, true)
            }
        }

        typingJob = viewModelScope.launch {
            delay(3000)
            isCurrentlyTyping = false
            repository.notifyTyping(id, false)
        }
    }

    private fun observeIncomingMessages() {
        // Observer for new incoming messages
        viewModelScope.launch {
            repository.incomingMessages.collect { message ->
                message?.let { msg ->
                    val activeId = currentConversationId.value
                    val receiverId = targetReceiverId

                    if (activeId != null && msg.conversationId == activeId) {
                        addMessageToList(msg)
                        markAsRead()
                    } 
                    // BUG FIX: Transición de "new" a ID real
                    else if (activeId == "new" && receiverId != null) {
                        if (msg.senderId == receiverId || msg.receiverId == receiverId) {
                            Log.d("ChatViewModel", "Switching from 'new' to real ID: ${msg.conversationId}")
                            currentConversationId.value = msg.conversationId
                            addMessageToList(msg)
                            markAsRead()
                        }
                    }
                }
            }
        }

        // Observer for global WebSocket events (like Read receipts)
        viewModelScope.launch {
            repository.webSocketEvents.collect { event ->
                val activeId = currentConversationId.value
                when (event) {
                    is WebSocketEvent.MessageRead -> {
                        if (activeId != null && event.conversationId == activeId) {
                            // Update all my sent messages to READ
                            val updatedMessages = _messages.value.map { msg ->
                                // If I am the sender, mark it as read when the other person reads the conversation
                                if (msg.senderId != targetReceiverId && msg.status != MessageStatus.READ) {
                                    msg.copy(status = MessageStatus.READ)
                                } else {
                                    msg
                                }
                            }
                            _messages.value = updatedMessages
                            Log.d("ChatViewModel", "Updated read receipts locally for conversation: $activeId")
                        }
                    }
                    is WebSocketEvent.TypingEvent -> {
                        if (activeId != null && event.conversationId == activeId) {
                            _isReceiverTyping.value = event.isTyping
                        }
                    }
                    else -> {}
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

    fun clearError() { _uiState.value = ChatUiState.Success }

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
