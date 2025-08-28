package com.example.neuxum_cliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neuxum_cliente.app.CustomerApp
import com.example.neuxum_cliente.ui.components.WheelPicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomerApp()
//            WheelPickerTest()
        }
    }

    @Composable
    private fun WheelPickerTest() {
        Column {
            val _items = listOf(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
                "Item 9",
                "Item 10"
            )
            var items: SnapshotStateList<String> =
                remember { mutableStateListOf(*_items.toTypedArray()) }
            var seleccionado by remember { mutableStateOf<String>("") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                WheelPicker(
                    items = items,
                    initialIndex = items.indexOf(seleccionado),
                    onSelectedIndex = { index ->
                        seleccionado = items[index]
                    },
                    modifier = Modifier.width(70.dp)
                )

            }
            Text(text = seleccionado)
        }
    }
}