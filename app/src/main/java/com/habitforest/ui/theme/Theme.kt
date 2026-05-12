package com.habitforest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Forest Palette ──────────────────────────────────────────────────────────
val ForestGreen  = Color(0xFF2D6A4F)
val LeafGreen    = Color(0xFF52B788)
val MossGreen    = Color(0xFF74C69D)
val SkyBlue      = Color(0xFFB7E4C7)
val EarthBrown   = Color(0xFF8B5E3C)
val GoldenYes    = Color(0xFFFFB703)
val CoralNo      = Color(0xFFFF6B6B)
val NightForest  = Color(0xFF081C15)
val DarkMoss     = Color(0xFF1B4332)
val ParchmentBg  = Color(0xFFF0F7EE)
val LightSurface = Color(0xFFFFFFFF)

private val LightColors = lightColorScheme(
    primary          = ForestGreen,
    onPrimary        = Color.White,
    primaryContainer = SkyBlue,
    secondary        = LeafGreen,
    onSecondary      = Color.White,
    background       = ParchmentBg,
    surface          = LightSurface,
    onBackground     = NightForest,
    onSurface        = NightForest,
    tertiary         = GoldenYes,
    error            = CoralNo
)

@Composable
fun HabitForestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
