package com.example.dox.ui.theme

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

private val NothingDarkColorScheme = darkColorScheme(
    primary = NothingRed,
    onPrimary = NothingWhite,
    primaryContainer = NothingRedDark,
    onPrimaryContainer = NothingWhite,
    
    secondary = NothingDarkGray,
    onSecondary = NothingWhite,
    secondaryContainer = NothingDarkSurfaceVariant,
    onSecondaryContainer = NothingWhite,
    
    tertiary = NothingDarkGray,
    onTertiary = NothingWhite,
    
    background = NothingDarkBackground,
    onBackground = NothingWhite,
    
    surface = NothingDarkSurface,
    onSurface = NothingWhite,
    surfaceVariant = NothingDarkSurfaceVariant,
    onSurfaceVariant = NothingMediumGray,
    
    error = NothingRed,
    onError = NothingWhite,
    errorContainer = NothingRedDark,
    onErrorContainer = NothingWhite,
    
    outline = NothingDarkGray,
    outlineVariant = NothingDarkSurfaceVariant
)

private val NothingLightColorScheme = lightColorScheme(
    primary = NothingRed,
    onPrimary = NothingWhite,
    primaryContainer = NothingLightGray,
    onPrimaryContainer = NothingBlack,
    
    secondary = NothingDarkGray,
    onSecondary = NothingWhite,
    secondaryContainer = NothingLightGray,
    onSecondaryContainer = NothingBlack,
    
    tertiary = NothingDarkGray,
    onTertiary = NothingWhite,
    
    background = NothingWhite,
    onBackground = NothingBlack,
    
    surface = NothingWhite,
    onSurface = NothingBlack,
    surfaceVariant = NothingLightGray,
    onSurfaceVariant = NothingDarkGray,
    
    error = NothingRed,
    onError = NothingWhite,
    errorContainer = NothingLightGray,
    onErrorContainer = NothingRed,
    
    outline = NothingMediumGray,
    outlineVariant = NothingLightGray
)

@Composable
fun DoXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled for Nothing OS design consistency
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> NothingDarkColorScheme
        else -> NothingLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NothingTypography,
        content = content
    )
}