package com.example.ejercicio1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ShopColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = SurfaceWhite,
    secondary = Violet,
    tertiary = Coral,
    background = Background,
    surface = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextMuted
)

@Composable
fun Ejercicio1Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ShopColorScheme,
        typography = Typography, // viene de Type.kt (lo crea Android Studio)
        content = content
    )
}