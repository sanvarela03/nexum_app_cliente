package com.example.nexum_cliente.ui.presenter.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.nexum_cliente.ui.navigation.rutes.PlaygroundRoutes

// ---------------------------------------------------------------------------
// Data model for a playground entry
// ---------------------------------------------------------------------------

data class PlaygroundComponent(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: PlaygroundRoutes
)

private val playgroundComponents = listOf(
    PlaygroundComponent(
        title = "Credit Card Form",
        description = "Formulario de tarjeta de crédito con validación y detección de red",
        icon = Icons.Filled.CreditCard,
        route = PlaygroundRoutes.CreditCardForm
    )
)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(
    onNavigateTo: (PlaygroundRoutes) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playground") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playgroundComponents) { component ->
                PlaygroundComponentCard(
                    component = component,
                    onClick = { onNavigateTo(component.route) }
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Component card
// ---------------------------------------------------------------------------

@Composable
private fun PlaygroundComponentCard(
    component: PlaygroundComponent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = component.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = component.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = component.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
