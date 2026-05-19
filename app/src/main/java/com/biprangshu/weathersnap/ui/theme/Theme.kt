package com.biprangshu.weathersnap.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val WeatherSnapDarkColorScheme = darkColorScheme(
    primary = LimeAccent,
    onPrimary = DarkOliveText,
    background = DarkBackground,
    onBackground = OnDarkSurface,
    surface = DarkSurface,
    onSurface = OnDarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSurfaceVariant,
    surfaceContainer = DarkSurfaceVariant,
    surfaceContainerHigh = DarkSurfaceVariant,
)

@Composable
fun WeatherSnapTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = WeatherSnapDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
