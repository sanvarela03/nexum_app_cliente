package com.example.neuxum_cliente.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.neuxum_cliente.data.market_location.local.MarketLocationEntity
import com.example.neuxum_cliente.ui.presenter.sign_up.CityState

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 19/08/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CitiesComponent(
    initialSelectedCity: CityState? = null,
    cities: List<CityState> = emptyList<CityState>(),
    onCitySelected: (CityState) -> Unit = {},
    maxHeight: Dp = 340.dp
) {
    var selectedCity = initialSelectedCity ?: cities.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)      // <- cap height
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE6E6E6), RoundedCornerShape(12.dp)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(cities) { city ->
            CityItem(
                text = city.city,
                isSelected = selectedCity == city,
                onClick = {
                    selectedCity = city
                    onCitySelected(city)
                }
            )
        }
    }
}
