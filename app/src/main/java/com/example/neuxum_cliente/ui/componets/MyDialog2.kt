package com.example.neuxum_cliente.ui.componets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


@Composable
fun MyDialog2(
    tittle: String,
    show: Boolean,
    dismissTxt: String = "Rechazar",
    confirmTxt: String = "Confirmar",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (show) {
        AlertDialog(
            title = {
                Text(
                    text = tittle,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.fillMaxWidth()
                )
            },
            text = {
                content()
            },
            onDismissRequest = { onDismiss() },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = dismissTxt,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = confirmTxt, color = Color.Black)
                }
            },
            modifier = modifier,
        )
    }
}