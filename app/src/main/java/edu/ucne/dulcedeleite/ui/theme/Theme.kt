package edu.ucne.dulcedeleite.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    onBackground = DarkText,
    primary = DarkText,
    surface = DarkBackground,
    onSurface = DarkText
)

private val LightColorScheme = lightColorScheme(
    background = LightBackground,
    onBackground = LightText,
    primary = LightText,
    surface = LightBackground,
    onSurface = LightText
)

@Composable
fun DulceDeleiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}