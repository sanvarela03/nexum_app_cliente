package com.example.nexum_cliente.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun surfaceTextFieldColors(
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.outline
): TextFieldColors {
    val colors = MaterialTheme.colorScheme
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = colors.surfaceContainerHighest,
        unfocusedContainerColor = colors.surfaceContainerLow,
        disabledContainerColor = colors.surfaceContainerLow,
        errorContainerColor = colors.surfaceContainerLow,
        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        disabledTextColor = colors.onSurface.copy(alpha = 0.38f),
        errorTextColor = colors.onSurface,
        focusedBorderColor = colors.onSurfaceVariant,
        unfocusedBorderColor = unfocusedBorderColor,
        disabledBorderColor = colors.outline.copy(alpha = 0.12f),
        errorBorderColor = colors.error,
        focusedLabelColor = colors.onSurface,
        unfocusedLabelColor = colors.onSurfaceVariant,
        disabledLabelColor = colors.onSurface.copy(alpha = 0.38f),
        errorLabelColor = colors.error,
        focusedLeadingIconColor = colors.onSurface,
        unfocusedLeadingIconColor = colors.onSurfaceVariant,
        disabledLeadingIconColor = colors.onSurface.copy(alpha = 0.38f),
        errorLeadingIconColor = colors.error,
        focusedTrailingIconColor = colors.onSurface,
        unfocusedTrailingIconColor = colors.onSurfaceVariant,
        disabledTrailingIconColor = colors.onSurface.copy(alpha = 0.38f),
        errorTrailingIconColor = colors.error,
        focusedPlaceholderColor = colors.onSurfaceVariant,
        unfocusedPlaceholderColor = colors.onSurfaceVariant,
        cursorColor = colors.onSurface,
        errorCursorColor = colors.error,
        selectionColors = TextSelectionColors(
            handleColor = colors.onSurfaceVariant,
            backgroundColor = colors.onSurface.copy(alpha = 0.2f)
        )
    )
}
