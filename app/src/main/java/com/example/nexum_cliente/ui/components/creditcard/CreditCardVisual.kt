package com.example.nexum_cliente.ui.components.creditcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CardColorVisa = listOf(Color(0xFF1A1F71), Color(0xFF2A3F99))
private val CardColorMastercard = listOf(Color(0xFF231F20), Color(0xFFEB001B), Color(0xFFF79E1B))
private val CardColorAmex = listOf(Color(0xFF016FD0), Color(0xFF2E9FFF))
private val CardColorUnknown = listOf(Color(0xFF2D2D2D), Color(0xFF5A5A5A))

private fun cardColors(cardType: CardType): List<Color> = when (cardType) {
    CardType.VISA -> CardColorVisa
    CardType.MASTERCARD -> CardColorMastercard
    CardType.AMEX -> CardColorAmex
    CardType.UNKNOWN -> CardColorUnknown
}

private fun chipColor(cardType: CardType): Color = when (cardType) {
    CardType.MASTERCARD -> Color(0xFFD4AF37)
    else -> Color(0xFFD4AF37)
}

@Composable
fun CreditCardVisual(
    state: CreditCardState,
    modifier: Modifier = Modifier
) {
    val placeholderChar = '·'
    val cardNumberDisplay = buildCardNumberDisplay(
        raw = state.cardNumber,
        placeholder = placeholderChar
    )
    val nameDisplay = state.cardHolderName.ifEmpty { "NOMBRE DEL TITULAR" }
    val expiryDisplay = buildExpiryDisplay(state.expirationDate)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(cardColors(state.cardType)),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row: network label + card type name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.cardType.label().ifEmpty { "CREDIT CARD" },
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Chip
                Box(
                    modifier = Modifier
                        .size(36.dp, 26.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(chipColor(state.cardType).copy(alpha = 0.8f))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card number
                Text(
                    text = cardNumberDisplay,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom row: name + expiry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = nameDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = expiryDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

private fun buildCardNumberDisplay(raw: String, placeholder: Char): String {
    val padded = raw.padEnd(16, placeholder)
    return padded.chunked(4).joinToString(" ")
}

private fun buildExpiryDisplay(raw: String): String {
    if (raw.isEmpty()) return "MM/AA"
    val padded = raw.padEnd(4, '·')
    val m = padded.take(2)
    val y = padded.drop(2).take(2)
    return "$m/$y"
}
