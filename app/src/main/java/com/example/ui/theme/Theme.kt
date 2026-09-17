package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AmberGoldPrimary,
    onPrimary = ObsidianBg,
    primaryContainer = SurfaceCardElevated,
    onPrimaryContainer = AmberGoldLight,
    secondary = EmeraldBreak,
    onSecondary = ObsidianBg,
    secondaryContainer = SurfaceCard,
    onSecondaryContainer = EmeraldLight,
    tertiary = CyanBreak,
    onTertiary = ObsidianBg,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderGold
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
