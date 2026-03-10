package com.example.nexum_cliente.ui.presenter.conversations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_cliente.domain.model.Conversation
import com.example.nexum_cliente.ui.presenter.chat.ChatUiState
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.5
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationsScreen(
    currentUserId: String,
    onConversationClick: (conversationId: String, otherUserId: String, otherUserRole: String) -> Unit,
    viewModel: ConversationsViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()
    Nexum_clienteTheme {
        ConversationsScreenContent(
            currentUserId = currentUserId,
            conversations = conversations,
            uiState = uiState,
            unreadCount = unreadCount,
            onRefresh = { viewModel.loadConversations(refresh = true) },
            onUpdateUnread = { viewModel.loadUnreadCount() },
            onConversationClick = onConversationClick,
            onNewChatClick = { /* TODO: Implementar lógica de nuevo chat */ }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationsScreenContent(
    currentUserId: String,
    conversations: List<Conversation>,
    uiState: ConversationsUiState,
    unreadCount: Long,
    onRefresh: () -> Unit,
    onUpdateUnread: () -> Unit,
    onConversationClick: (String, String, String) -> Unit,
    onNewChatClick: () -> Unit
) {
    val isRefreshing = uiState is ConversationsUiState.Refreshing

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Column {
                        Text("Mensajes")
                        if (unreadCount > 0) {
                            Text(
                                text = "$unreadCount no leídos",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onUpdateUnread) {
                        Icon(Icons.Default.Chat, "Actualizar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewChatClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.AddComment,
                    contentDescription = "Nuevo mensaje"
                )
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = onRefresh,
            modifier = Modifier.padding(paddingValues)
        ) {
            when (uiState) {
                is ConversationsUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ConversationsUiState.Error -> {
                    ErrorMessage(
                        message = (uiState as ChatUiState.Error).message,
                        onRetry = onRefresh
                    )
                }

                else -> {
                    if (conversations.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(items = conversations, key = { it.id }) { conversation ->
                                ConversationItem(
                                    conversation = conversation,
                                    currentUserId = currentUserId,
                                    onClick = {
                                        val otherUserId = conversation.participantIds
                                            .first { it != currentUserId }
                                        val otherUserRole = conversation.participantRoles
                                            .first()
                                        onConversationClick(
                                            conversation.id,
                                            otherUserId,
                                            otherUserRole
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ConversationItem(
    conversation: Conversation,
    currentUserId: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Conversación ${conversation.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                conversation.lastMessageContent?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                conversation.lastMessageAt?.let {
                    Text(
                        text = formatDateTime(it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (conversation.unreadCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
    HorizontalDivider()
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Chat,
                null,
                Modifier.size(64.dp),
                MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "No hay conversaciones",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val messageDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val messageDate = messageDateTime.toLocalDate()

        when {
            messageDate.isEqual(today) -> DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
                .format(messageDateTime)

            messageDate.year == today.year && messageDate.get(
                WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
            ) == today.get(
                WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
            ) -> DateTimeFormatter.ofPattern("EEE h:mm a", Locale.getDefault())
                .format(messageDateTime)

            else -> DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                .format(messageDateTime)
        }
    } catch (e: Exception) {
        timestamp
    }
}
