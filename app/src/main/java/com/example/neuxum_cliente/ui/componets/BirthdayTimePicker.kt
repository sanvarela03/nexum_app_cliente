package com.example.neuxum_cliente.ui.componets

// ---------- Imports ----------
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt


// ---------- Utils (API-24 safe) ----------
private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (isLeapYear(year)) 29 else 28
    else -> 30
}

// ---------- Public API ----------
@Composable
fun DateOfBirthPicker(
    modifier: Modifier = Modifier,
    // integers instead of LocalDate to avoid API-26 requirement
    initialYear: Int = 1990,
    initialMonth: Int = 1, // 1..12
    initialDay: Int = 1,   // 1..31 (will be coerced)
    minYear: Int = 1900,
    maxYear: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
    onDateChanged: (year: Int, month: Int, day: Int) -> Unit
) {
    var selectedYear by remember { mutableIntStateOf(initialYear.coerceIn(minYear, maxYear)) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth.coerceIn(1, 12)) }
    var selectedDay by remember { mutableIntStateOf(initialDay.coerceIn(1, 31)) }

    // Re-coerce the day whenever year/month changes
    val maxDay = daysInMonth(selectedYear, selectedMonth)
    if (selectedDay > maxDay) selectedDay = maxDay

    // Notify parent on any change
    LaunchedEffect(selectedYear, selectedMonth, selectedDay) {
        onDateChanged(selectedYear, selectedMonth, selectedDay)
    }

    val borderColor = Color(0xFFE6E6E6)

    Box(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        // center highlight band (like iOS/your screenshot)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF2F2F2))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // DAY (fixed width)
            WheelPicker(
                items = (1..maxDay).map { it.toString().padStart(2, '0') },
                initialIndex = (selectedDay - 1),
                onSelectedIndex = { selectedDay = it + 1 },
                modifier = Modifier.width(70.dp)
            )

            // MONTH (takes remaining space via weight)
            val monthLabels = remember {
                listOf(
                    "enero","febrero","marzo","abril","mayo","junio",
                    "julio","agosto","septiembre","octubre","noviembre","diciembre"
                )
            }
            WheelPicker(
                items = monthLabels,
                initialIndex = (selectedMonth - 1),
                onSelectedIndex = { selectedMonth = it + 1 },
                modifier = Modifier.weight(1f) // ✅ works because inside Row scope + import
            )

            // YEAR (fixed width)
            val years = remember(minYear, maxYear) { (minYear..maxYear).toList() }
            WheelPicker(
                items = years.map { it.toString() },
                initialIndex = years.indexOf(selectedYear).coerceAtLeast(0),
                onSelectedIndex = { selectedYear = years[it] },
                modifier = Modifier.width(90.dp)
            )
        }
    }
}

// ---------- Wheel Picker ----------
@Composable
fun WheelPicker(
    items: List<String>,
    initialIndex: Int = 0,
    onSelectedIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 36.dp,
    visibleCount: Int = 5 // should be odd for perfect centering
) {
    val state = rememberLazyListState(
        // Start roughly centered on initialIndex
        initialFirstVisibleItemIndex = (initialIndex - visibleCount / 2)
            .coerceIn(0, (items.lastIndex).coerceAtLeast(0))
    )
    val fling = rememberSnapFlingBehavior(lazyListState = state)
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    // Compute which item is visually centered
    val centeredIndex by remember {
        derivedStateOf {
            val offsetItems = state.firstVisibleItemScrollOffset / itemHeightPx
            (state.firstVisibleItemIndex + offsetItems.roundToInt() + visibleCount / 2)
                .coerceIn(0, items.lastIndex)
        }
    }

    // Notify when center changes
    LaunchedEffect(centeredIndex) { onSelectedIndex(centeredIndex) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val sideItems = (visibleCount - 1) / 2f

        LazyColumn(
            state = state,
            flingBehavior = fling,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = itemHeight * sideItems), // ✅ Dp * Float
            userScrollEnabled = true
        ) {
            itemsIndexed(items) { index, label ->
                val distance = abs(index - centeredIndex)
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.6f
                    2 -> 0.35f
                    else -> 0.2f
                }
                val size = if (distance == 0) 18.sp else 16.sp
                val weight = if (distance == 0) FontWeight.SemiBold else FontWeight.Normal

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = size,
                        fontWeight = weight,
                        color = Color.Black.copy(alpha = alpha)
                    )
                }
            }
        }
    }
}

// ---------- Example usage ----------
@Composable
fun BirthdayTimePicker() {
    var year by remember { mutableIntStateOf(1978) }
    var month by remember { mutableIntStateOf(9) }
    var day by remember { mutableIntStateOf(3) }

    Column(Modifier.padding(16.dp)) {
        Text("¿Cuál es tu fecha de nacimiento?", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("Fecha de nacimiento", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(8.dp))

        DateOfBirthPicker(
            initialYear = year,
            initialMonth = month,
            initialDay = day,
            minYear = 1950,
            onDateChanged = { y, m, d ->
                year = y; month = m; day = d
            }
        )

        Spacer(Modifier.height(12.dp))
        Text("Seleccionado: %04d-%02d-%02d".format(year, month, day))
    }
}
