package com.example.ui.theme

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
    primary = TelegramBlue,
    onPrimary = TelegramWhite,
    primaryContainer = TelegramNavy,
    onPrimaryContainer = TelegramLightBlue,
    secondary = TelegramLightBlue,
    onSecondary = TelegramNavy,
    tertiary = TelegramBadgeGreen,
    background = TelegramBackgroundDark,
    onBackground = TelegramTextPrimary,
    surface = TelegramCardDark,
    onSurface = TelegramTextPrimary,
    surfaceVariant = TelegramNavy,
    onSurfaceVariant = TelegramTextSecondary,
    outline = TelegramCardStrokeDark
)

private val LightColorScheme = lightColorScheme(
    primary = TelegramDarkBlue,
    onPrimary = TelegramWhite,
    primaryContainer = TelegramLightBlue.copy(alpha = 0.2f),
    onPrimaryContainer = TelegramDarkBlue,
    secondary = TelegramBlue,
    onSecondary = TelegramWhite,
    tertiary = TelegramBadgeGreen,
    background = TelegramBackgroundLight,
    onBackground = TelegramTextPrimaryLight,
    surface = TelegramSurfaceLight,
    onSurface = TelegramTextPrimaryLight,
    surfaceVariant = TelegramCardStrokeLight,
    onSurfaceVariant = TelegramTextSecondaryLight,
    outline = TelegramCardStrokeLight
)

@Composable
fun TeleGrabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Telegram apps have strong distinct branding
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
