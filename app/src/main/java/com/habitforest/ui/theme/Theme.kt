package com.habitforest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Game RPG Palette ────────────────────────────────────────────────────────
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

// New Game-Specific Colors
val GameGold      = Color(0xFFFFD700)
val GameOrange    = Color(0xFFFB8500)
val GameParchment = Color(0xFFFEFAE0)
val GameParchmentShadow = Color(0xFFF1E4C0)
val DeepWood      = Color(0xFF283618)
val SoftWood      = Color(0xFF606C38)
val BorderBrown   = Color(0xFF432818)
val HealthRed     = Color(0xFFE63946)
val ManaBlue      = Color(0xFF457B9D)

private val LightColors = lightColorScheme(
    primary          = ForestGreen,
    onPrimary        = Color.White,
    primaryContainer = SkyBlue,
    secondary        = LeafGreen,
    onSecondary      = Color.White,
    background       = GameParchment,
    surface          = GameParchment,
    onBackground     = DeepWood,
    onSurface        = DeepWood,
    tertiary         = GameGold,
    error            = HealthRed
)

val GameTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        letterSpacing = 0.15.sp,
        color = DeepWood
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        color = DeepWood
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        color = DeepWood
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        color = SoftWood
    )
)

@Composable
fun HabitForestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = GameTypography,
        content = content
    )
}
