package com.example.neuxum_cliente.ui.componets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = "or",
            fontSize = 18.sp
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray, thickness = 1.dp
        )
    }
}