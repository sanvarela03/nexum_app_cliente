package com.example.nexum_cliente.ui.presenter.conversations

<<<<<<< Updated upstream
=======
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.domain.model.Conversation
import com.example.nexum_cliente.domain.repository.MessagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class ConversationsViewModel {
=======
@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val repository: MessagingRepository
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _uiState = MutableStateFlow<ConversationsUiState>(ConversationsUiState.Loading)
    val uiState: StateFlow<ConversationsUiState> = _uiState.asStateFlow()

    private val _unreadCount = MutableStateFlow(0L)
    val unreadCount: StateFlow<Long> = _unreadCount.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private var canLoadMore = true

    init {
        loadConversations()
        loadUnreadCount()
        observeIncomingMessages()
    }

    fun loadConversations(refresh: Boolean = false) {
        if (refresh) {
            currentPage = 0
            canLoadMore = true
            _conversations.value = emptyList()
        }

        if (!canLoadMore && !refresh) return

        viewModelScope.launch {
            _uiState.value = if (refresh) {
                ConversationsUiState.Refreshing
            } else {
                ConversationsUiState.Loading
            }

            repository.getConversations(currentPage, pageSize)
                .onSuccess { response ->
                    val newConversations = if (refresh) {
                        response.content
                    } else {
                        _conversations.value + response.content
                    }
                    _conversations.value = newConversations
                    currentPage++
                    canLoadMore = response.content.size == pageSize
                    _uiState.value = ConversationsUiState.Success
                }
                .onFailure { error ->
                    Log.e("ConversationsViewModel", "Error loading conversations", error)
                    _uiState.value = ConversationsUiState.Error(
                        error.message ?: "Error loading conversations"
                    )
                }
        }
    }

    fun loadUnreadCount() {
        viewModelScope.launch {
            repository.getUnreadCount()
                .onSuccess { count ->
                    _unreadCount.value = count
                }
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            repository.incomingMessages.collect { newMessage ->
                newMessage?.let {
                    // Actualizar la conversación correspondiente
                    updateConversationWithNewMessage(it.conversationId)

                    // Actualizar contador de no leídos
                    loadUnreadCount()
                }
            }
        }
    }

    private fun updateConversationWithNewMessage(conversationId: String) {
        viewModelScope.launch {
            // Recargar conversaciones para obtener datos actualizados
            loadConversations(refresh = true)
        }
    }

    fun clearError() {
        _uiState.value = ConversationsUiState.Success
    }
}

sealed class ConversationsUiState {
    object Loading : ConversationsUiState()
    object Refreshing : ConversationsUiState()
    object Success : ConversationsUiState()
    data class Error(val message: String) : ConversationsUiState()
>>>>>>> Stashed changes
}