package com.example.nexum_cliente.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) Color.White else Color.Black,
                )
            }
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Black,
        ),
        modifier = modifier // <--- ajusta al contenido
    )

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FilterChipComponent2(
    modifier: Modifier = Modifier
        .width(106.dp)
        .height(46.dp),
    text: String = "Ejemplo",
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    FilterChip(
        onClick = onClick,
        enabled = enabled,
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFD6D6D6)) else BorderStroke(
            0.dp,
            Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(106.dp)
            ) {
                Text(
                    text = text,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) Color.White else if (enabled) Color.Black else Color.Gray,
                )
            }
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Black,
            disabledContainerColor = Color(0xFFF2F2F2),
            disabledLabelColor = Color.Gray
        ),
        modifier = modifier // <--- ajusta al contenido
    )

}