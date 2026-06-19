package com.example.nexum_cliente.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.nexum_cliente.ui.theme.componentShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(componentShapes.small),
    labelValue: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTextSelected: (String) -> Unit,
    textValue: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    errorStatus: Boolean = false,
    errorMessage: String? = null,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    var hasBeenModified by rememberSaveable { mutableStateOf(false) }

    // El error solo se muestra si el estado es de error Y el usuario ha modificado el campo.
    val isActuallyError = errorStatus && hasBeenModified
    
    // El verde solo se muestra si NO hay error, el usuario ha interactuado y NO tiene el foco.
    val shouldShowSuccess = !errorStatus && hasBeenModified && !isFocused

    val unfocusedBorderColor = if (shouldShowSuccess) {
        Color.Green 
    } else {
        Color(0xFFE6E6E6)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textValue,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = unfocusedBorderColor,
                errorBorderColor = Color.Red,
                unfocusedLabelColor = Color(0xFF9EA1A5),
                unfocusedLeadingIconColor = Color(0xFF9EA1A5),
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
            keyboardOptions = keyboardOptions,
            singleLine = true,
            maxLines = 1,
            onValueChange = {
                hasBeenModified = true
                onTextSelected(it)
            },
            leadingIcon = leadingIcon?.let { nonNullIcon ->
                { Icon(nonNullIcon, contentDescription = null) }
            },
            trailingIcon = trailingIcon?.let { nonNullIcon ->
                { Icon(nonNullIcon, contentDescription = null) }
            },
            isError = isActuallyError
        )
        if (isActuallyError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
