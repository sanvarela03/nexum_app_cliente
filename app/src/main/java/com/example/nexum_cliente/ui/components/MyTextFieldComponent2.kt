package com.example.nexum_cliente.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.nexum_cliente.ui.theme.componentShapes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@Composable
fun MyTextFieldComponent2(
    labelValue: String,
    textValue: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(componentShapes.small),
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    var hasBeenModified by rememberSaveable { mutableStateOf(false) }

    val shouldShowError = errorStatus && hasBeenModified && !isFocused

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textValue,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Blue,        // cuando está enfocado
                unfocusedBorderColor = Color(0xFFE6E6E6), // cuando no está enfocado
                errorBorderColor = Color.Red,
                unfocusedLabelColor = Color(0xFFE6E6E6),
                unfocusedLeadingIconColor = Color(0xFFE6E6E6),
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = modifier.onFocusEvent {
                isFocused = it.isFocused
            },
            label = {
                Text(
                    text = labelValue,
                    fontWeight = FontWeight.Medium
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            maxLines = 1,
            onValueChange = {
                hasBeenModified = true
                onTextSelected(it)
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = shouldShowError
        )
        if (shouldShowError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
