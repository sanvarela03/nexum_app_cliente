package com.example.nexum_cliente.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF383838),
    onPrimaryContainer = Color.White,
    inversePrimary = Color.Black,
    secondary = Color(0xFFCACACA),
    onSecondary = Color(0xFF202020),
    secondaryContainer = Color(0xFF383838),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFFBDBDBD),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF383838),
    onTertiaryContainer = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFE6E6E6),
    surfaceVariant = Color(0xFF454545),
    onSurfaceVariant = Color(0xFFCACACA),
    surfaceTint = Color.White,
    inverseSurface = Color(0xFFE6E6E6),
    inverseOnSurface = Color(0xFF1C1C1C),
    outline = Color(0xFF8F8F8F),
    outlineVariant = Color(0xFF454545),
    surfaceDim = Color(0xFF121212),
    surfaceBright = Color(0xFF383838),
    surfaceContainerLowest = Color(0xFF0D0D0D),
    surfaceContainerLow = Color(0xFF1C1C1C),
    surfaceContainer = Color(0xFF202020),
    surfaceContainerHigh = Color(0xFF2A2A2A),
    surfaceContainerHighest = Color(0xFF353535),
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E0E0),
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.White,
    secondary = Color(0xFF555555),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0E0E0),
    onSecondaryContainer = Color.Black,
    tertiary = Color.Gray,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0E0E0),
    onTertiaryContainer = Color.Black,
    background = Color(0xFFFAFAFA),
    onBackground = Color.Black,
    surface = Color(0xFFE5E5E5),
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFDADADA),
    onSurfaceVariant = Color.Black,
    surfaceTint = Color.Black,
    inverseSurface = Color(0xFF303030),
    inverseOnSurface = Color.White,
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFFBDBDBD),
    surfaceDim = Color(0xFFD6D6D6),
    surfaceBright = Color(0xFFFAFAFA),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF0F0F0),
    surfaceContainer = Color(0xFFEAEAEA),
    surfaceContainerHigh = Color(0xFFE5E5E5),
    surfaceContainerHighest = Color(0xFFDADADA),
)

val MaterialTheme.jobOfferCardBackground: Color
    @Composable
    get() = if (colorScheme.background.luminance() < 0.5f) {
        Color(0xFF383838)
    } else {
        Color(0xFFE4DFE5)
    }

@Composable
fun Nexum_clienteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val stepperColors = if (darkTheme) {
        StepperColors(
            selected = colorScheme.primary,
            unselected = colorScheme.surfaceVariant,
            onSelected = colorScheme.onPrimary
        )
    } else {
        LightStepperColors
    }

    CompositionLocalProvider(LocalStepperColors provides stepperColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
