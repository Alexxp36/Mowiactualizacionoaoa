package com.miempresa.mowimarket.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MowiOrange,
    onPrimary = TextWhite,
    primaryContainer = MowiOrangeLight,
    onPrimaryContainer = TextPrimary,

    secondary = TextSecondary,
    onSecondary = TextWhite,
    secondaryContainer = BackgroundSecondary,
    onSecondaryContainer = TextPrimary,

    tertiary = Info,
    onTertiary = TextWhite,
    tertiaryContainer = MowiOrangeLight,
    onTertiaryContainer = TextPrimary,

    error = Error,
    onError = TextWhite,
    errorContainer = Error,
    onErrorContainer = TextWhite,

    background = BackgroundPrimary,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    outline = Border,
    outlineVariant = Border,

    scrim = TextPrimary.copy(alpha = 0.5f)
)

@Composable
fun MowiMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}