package com.example.nexum_cliente.ui.presenter.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.nexum_cliente.domain.model.ConnectionState
import com.example.nexum_cliente.domain.model.Message
import com.example.nexum_cliente.ui.theme.Nexum_clienteTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 3.6
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    conversationId: String,
    receiverId: String,
    receiverRole: String,
    currentUserId: String,
    onNavigateBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState(initial = ConnectionState.DISCONNECTED)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(conversationId, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.connectWebSocket()
                    viewModel.loadConversationMessages(conversationId)
                }
                Lifecycle.Event.ON_STOP -> {
                    viewModel.disconnectWebSocket()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.disconnectWebSocket()
        }
    }

    Nexum_clienteTheme {
        ChatScreenContent(
            messages = messages,
            uiState = uiState,
            connectionState = connectionState,
            currentUserId = currentUserId,
            receiverId = receiverId,
            onNavigateBack = onNavigateBack,
            onSendMessage = { content ->
                viewModel.sendMessage(receiverId, receiverRole, content)
            },
            onNotifyTyping = { viewModel.notifyTyping(it) },
            onRetry = {
                viewModel.clearError()
                viewModel.loadConversationMessages(conversationId)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(
    messages: List<Message>,
    uiState: ChatUiState,
    connectionState: ConnectionState,
    currentUserId: String,
    receiverId: String,
    onNavigateBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onNotifyTyping: (Boolean) -> Unit,
    onRetry: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val chatItems = remember(messages) {
        val items = mutableListOf<Any>()
        for (i in messages.indices) {
            val currentMessage = messages[i]
            items.add(currentMessage)
            val currentDate = Instant.parse(currentMessage.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
            val nextDate = if (i + 1 < messages.size) {
                Instant.parse(messages[i + 1].timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
            } else null
            if (currentDate != nextDate) items.add(currentDate)
        }
        items
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(0)
    }

    LaunchedEffect(messageText) {
        onNotifyTyping(messageText.isNotEmpty())
    }

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Chat con: $receiverId")
                            Text(
                                text = when (connectionState) {
                                    ConnectionState.CONNECTED -> "Conectado"
                                    ConnectionState.CONNECTING -> "Conectando..."
                                    ConnectionState.RECONNECTING -> "Reconectando..."
                                    ConnectionState.DISCONNECTED -> "Desconectado"
                                    ConnectionState.ERROR -> "Error de conexión"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = when (connectionState) {
                                    ConnectionState.CONNECTED -> Color(0xFF4CAF50)
                                    ConnectionState.RECONNECTING, ConnectionState.CONNECTING -> Color(0xFFFF9800)
                                    ConnectionState.ERROR -> Color.Red
                                    else -> Color.Gray
                                }
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                )
            }
        },
        bottomBar = {
            ChatInputBar(
                text = messageText,
                onTextChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText)
                        messageText = ""
                    }
                },
                enabled = connectionState == ConnectionState.CONNECTED
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is ChatUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is ChatUiState.Error -> ErrorMessage(uiState.message, onRetry)
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
                    ) {
                        items(items = chatItems, key = { item ->
                            when (item) {
                                is Message -> item.id
                                is LocalDate -> item.toString()
                                else -> item.hashCode()
                            }
                        }) { item ->
                            when (item) {
                                is Message -> MessageItem(item, item.senderId == currentUserId)
                                is LocalDate -> DateHeader(item)
                            }
                        }
                        if (uiState is ChatUiState.LoadingMore) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
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
private fun DateHeader(date: LocalDate) {
    val text = remember(date) {
        val today = LocalDate.now()
        when {
            date.isEqual(today) -> "Hoy"
            date.isEqual(today.minusDays(1)) -> "Ayer"
            else -> date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es", "ES")))
        }
    }
    Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), shape = RoundedCornerShape(16.dp)) {
            Text(text, Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MessageItem(message: Message, isCurrentUser: Boolean) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start) {
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isCurrentUser) 16.dp else 4.dp, bottomEnd = if (isCurrentUser) 4.dp else 16.dp),
            color = if (isCurrentUser) MaterialTheme.colorScheme.primary else Color.LightGray,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(message.content, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(formatTime(message.timestamp), style = MaterialTheme.typography.bodySmall)
                    if (isCurrentUser) {
                        Text(
                            text = if (message.readAt != null) "✓✓" else "✓",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (message.readAt != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()).withZone(ZoneId.systemDefault()).format(instant)
    } catch (e: Exception) { "" }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 3.dp,
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Transparent,
                    unfocusedLeadingIconColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .shadow(elevation = 5.dp, shape = CircleShape)
                    .background(Color.White, CircleShape),
                placeholder = { Text(text = "Escribe un mensaje...", fontSize = 13.sp, fontWeight = FontWeight.Light) },
                enabled = enabled,
                shape = RoundedCornerShape(30.dp),
                maxLines = 4
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                enabled = enabled && text.isNotBlank(),
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .background(
                        color = if (enabled && text.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray,
                        shape = CircleShape
                    ),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
