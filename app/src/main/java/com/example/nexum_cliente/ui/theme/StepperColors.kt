package com.example.nexum_cliente.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class StepperColors(
    val selected: Color,
    val unselected: Color,
    val onSelected: Color
)

internal val LightStepperColors = StepperColors(
    selected = Color(0xFF009963),
    unselected = Color(0xFFE6E6E6),
    onSelected = Color(0xFFD1CCD3)
)

internal val LocalStepperColors = staticCompositionLocalOf { LightStepperColors }

val MaterialTheme.stepperColors: StepperColors
    @Composable
    get() = LocalStepperColors.current
