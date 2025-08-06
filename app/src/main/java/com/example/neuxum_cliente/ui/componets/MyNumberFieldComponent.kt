package com.example.neuxum_cliente.ui.componets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.neuxum_cliente.ui.theme.componentShapes

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 24/07/2025
 * @version 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNumberFieldComponent(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(componentShapes.small),
    labelValue: String,
    icon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    focusHops: Int = 1
) {
    val textValue = rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = textValue.value,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,        // cuando está enfocado
            unfocusedBorderColor = Color(0xFFE6E6E6), // cuando no está enfocado
            errorBorderColor = Color.Red,
            unfocusedLabelColor =  Color(0xFFE6E6E6),
            unfocusedLeadingIconColor = Color(0xFFE6E6E6)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        label = { Text(
            text = labelValue,
            fontWeight = FontWeight.Medium
        )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                repeat (focusHops) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            }
        ),
        singleLine = true,
        maxLines = 1,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = icon?.let { nonNullIcon ->
            { Icon(nonNullIcon, contentDescription = null) }
        },
        trailingIcon = trailingIcon?.let { nonNullIcon ->
            { Icon(nonNullIcon, contentDescription = null) }
        },
        isError = !errorStatus
    )
}