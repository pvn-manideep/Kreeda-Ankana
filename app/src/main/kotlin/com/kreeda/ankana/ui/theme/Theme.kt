package com.kreeda.ankana.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Ultra-Modern Palette ─────────────────────────────────────────────────────
object KC {
    // Primary — Electric Violet to Magenta gradient brand
    val Violet        = Color(0xFF7C3AED)
    val VioletLight   = Color(0xFF9F67FF)
    val VioletDeep    = Color(0xFF5B21B6)
    val Magenta       = Color(0xFFD946EF)
    val Cyan          = Color(0xFF06B6D4)
    val CyanLight     = Color(0xFF22D3EE)

    // Sports accents
    val SportGreen    = Color(0xFF10B981)
    val SportGreenLt  = Color(0xFF34D399)
    val SportRed      = Color(0xFFF43F5E)
    val SportAmber    = Color(0xFFF59E0B)
    val SportBlue     = Color(0xFF3B82F6)

    // Dark theme — true deep surfaces
    val DarkBg        = Color(0xFF09090B)   // near-black zinc
    val DarkSurface   = Color(0xFF141417)
    val DarkCard      = Color(0xFF1C1C21)
    val DarkCardHigh  = Color(0xFF27272D)
    val DarkBorder    = Color(0xFF3F3F47)
    val DarkText      = Color(0xFFFAFAFA)
    val DarkSubText   = Color(0xFF71717A)

    // Light theme — crisp white with warmth
    val LightBg       = Color(0xFFFAFAFF)
    val LightSurface  = Color(0xFFFFFFFF)
    val LightCard     = Color(0xFFF4F4F8)
    val LightBorder   = Color(0xFFE4E4F0)
    val LightText     = Color(0xFF18181B)
    val LightSubText  = Color(0xFF71717A)

    // Glassmorphism overlays
    val GlassLight    = Color(0x1AFFFFFF)
    val GlassDark     = Color(0x0DFFFFFF)

    // Gradient combos
    val GradPrimary   = listOf(Violet, Magenta)
    val GradCool      = listOf(Violet, Cyan)
    val GradFire      = listOf(Color(0xFFFF6B35), Magenta)
    val GradNight     = listOf(DarkBg, Color(0xFF0F0F1A))
}

// ── App Theme State (for toggle) ─────────────────────────────────────────────
object ThemeState {
    var isDark by mutableStateOf(true)
}

private val DarkScheme = darkColorScheme(
    primary          = KC.Violet,
    onPrimary        = Color.White,
    primaryContainer = KC.VioletDeep.copy(alpha = 0.4f),
    onPrimaryContainer = KC.VioletLight,
    secondary        = KC.Cyan,
    onSecondary      = Color.White,
    secondaryContainer = KC.Cyan.copy(alpha = 0.2f),
    tertiary         = KC.Magenta,
    background       = KC.DarkBg,
    surface          = KC.DarkSurface,
    surfaceVariant   = KC.DarkCard,
    onBackground     = KC.DarkText,
    onSurface        = KC.DarkText,
    onSurfaceVariant = KC.DarkSubText,
    outline          = KC.DarkBorder,
    error            = KC.SportRed
)

private val LightScheme = lightColorScheme(
    primary          = KC.VioletDeep,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = KC.VioletDeep,
    secondary        = KC.Cyan,
    onSecondary      = Color.White,
    secondaryContainer = Color(0xFFE0F9FF),
    tertiary         = KC.Magenta,
    background       = KC.LightBg,
    surface          = KC.LightSurface,
    surfaceVariant   = KC.LightCard,
    onBackground     = KC.LightText,
    onSurface        = KC.LightText,
    onSurfaceVariant = KC.LightSubText,
    outline          = KC.LightBorder,
    error            = KC.SportRed
)

@Composable
fun KreedaTheme(
    darkTheme: Boolean = ThemeState.isDark,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = Typography(
            displayLarge  = TextStyle(fontWeight = FontWeight.Black,     fontSize = 40.sp, letterSpacing = (-1.5).sp),
            displayMedium = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, letterSpacing = (-1).sp),
            headlineLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, letterSpacing = (-0.5).sp),
            headlineMedium= TextStyle(fontWeight = FontWeight.Bold,      fontSize = 22.sp),
            titleLarge    = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 18.sp),
            titleMedium   = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 15.sp),
            bodyLarge     = TextStyle(fontSize = 16.sp, letterSpacing = 0.sp),
            bodyMedium    = TextStyle(fontSize = 14.sp),
            labelLarge    = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 13.sp, letterSpacing = 0.1.sp),
            labelSmall    = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 10.sp, letterSpacing = 1.2.sp)
        ),
        content = content
    )
}
