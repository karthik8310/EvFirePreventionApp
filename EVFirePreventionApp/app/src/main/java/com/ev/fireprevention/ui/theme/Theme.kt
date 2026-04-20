package com.ev.fireprevention.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    secondary = BrandSecondary,
    tertiary = BrandAccent,
    background = BrandDarkBackground,
    surface = BrandSurface,
    onPrimary = BrandDarkBackground,
    onSecondary = BrandTextPrimary,
    onTertiary = BrandTextPrimary,
    onBackground = BrandTextPrimary,
    onSurface = BrandTextPrimary,
    surfaceVariant = BrandSurfaceVariant,
    onSurfaceVariant = BrandTextSecondary,
    error = BrandError
)

private val LightColorScheme = darkColorScheme( // Force Dark Theme for Premium feel
    primary = BrandPrimary,
    secondary = BrandSecondary,
    tertiary = BrandAccent,
    background = BrandDarkBackground,
    surface = BrandSurface,
    onPrimary = BrandDarkBackground,
    onSecondary = BrandTextPrimary,
    onTertiary = BrandTextPrimary,
    onBackground = BrandTextPrimary,
    onSurface = BrandTextPrimary,
    surfaceVariant = BrandSurfaceVariant,
    onSurfaceVariant = BrandTextSecondary,
    error = BrandError
)

@Composable
fun EVFirePreventionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color to enforce brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Always use Dark Premium Theme for now


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}