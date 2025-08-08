package com.example.neuxum_cliente.ui.componets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FilterChipComponent(
    text: String = "Ejemplo",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    FilterChip(
        onClick = onClick,
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFD6D6D6)) else BorderStroke(
            0.dp,
            Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                text = text,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Black,
                modifier = Modifier.padding(horizontal = 1.dp) // <--- reduce ancho
            )
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Black,
        ),
        modifier = Modifier.defaultMinSize(minHeight = 27.dp) // <--- ajusta al contenido
    )

}