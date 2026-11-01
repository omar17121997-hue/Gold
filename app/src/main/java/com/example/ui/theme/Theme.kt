package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GoldDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = ObsidianBlack,
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    inversePrimary = GoldDark,
    secondary = GoldLight,
    onSecondary = ObsidianBlack,
    secondaryContainer = ObsidianCardBorder,
    onSecondaryContainer = GoldLight,
    tertiary = AccentChampagne,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPearlWhite,
    surface = ObsidianSurface,
    onSurface = TextPearlWhite,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = TextMutedGold,
    outline = ObsidianCardBorder,
    outlineVariant = Color(0xFF4A442E)
)

private val GoldLightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF3D0),
    onPrimaryContainer = Color(0xFF4E3A00),
    secondary = GoldPrimary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFBF4DC),
    onSecondaryContainer = Color(0xFF3B2E05),
    tertiary = AccentBronze,
    background = Color(0xFFFBFBFB),
    onBackground = Color(0xFF18181B),
    surface = Color.White,
    onSurface = Color(0xFF18181B),
    surfaceVariant = Color(0xFFF3F1EC),
    onSurfaceVariant = Color(0xFF534F44),
    outline = Color(0xFFDACFBA)
)

@Composable
fun GoldExpertTheme(
    darkTheme: Boolean = true, // Luxury dark gold default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GoldDarkColorScheme else GoldLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    GoldExpertTheme(darkTheme = true, content = content)
}
