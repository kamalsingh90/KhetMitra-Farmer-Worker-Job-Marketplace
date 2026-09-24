package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = Color(0xFF00390E),
    primaryContainer = ForestGreenDark,
    onPrimaryContainer = PrimaryContainer,
    secondary = GoldenWheatLight,
    onSecondary = Color(0xFF452200),
    secondaryContainer = OnSecondaryContainer,
    onSecondaryContainer = GoldenWheatContainer,
    background = Color(0xFF111411),
    surface = Color(0xFF191C19),
    surfaceVariant = Color(0xFF282F26),
    onBackground = Color(0xFFE2E3DE),
    onSurface = Color(0xFFE2E3DE)
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = GoldenWheatSecondary,
    onSecondary = Color.White,
    secondaryContainer = GoldenWheatContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TerracottaTertiary,
    tertiaryContainer = TerracottaContainer,
    background = AppBackground,
    surface = AppSurface,
    surfaceVariant = AppSurfaceVariant,
    outline = AppOutline,
    onBackground = AppTextPrimary,
    onSurface = AppTextPrimary,
    onSurfaceVariant = AppTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our rich agrarian branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
