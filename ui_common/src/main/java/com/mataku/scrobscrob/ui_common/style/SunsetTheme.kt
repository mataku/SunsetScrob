package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.core.entity.AppTheme

@Composable
fun SunsetTheme(
    theme: AppTheme = AppTheme.DARK,
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        theme.backgroundColor()
    )
    systemUiController.setNavigationBarColor(
        theme.colors().primary
    )
    val rippleTheme = SunsetRippleTheme(theme)
    MaterialTheme(
        colors = theme.colors()
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides rippleTheme,
            LocalAppTheme provides theme,
            content = content
        )
    }
}

val LocalAppTheme = staticCompositionLocalOf { AppTheme.DARK }
fun AppTheme.backgroundColor(): Color {
    return this.colors().background
}

fun AppTheme.colors(): androidx.compose.material.Colors {
    return when (this) {
        AppTheme.DARK -> {
            darkColors
        }
        AppTheme.LIGHT -> {
            lightColors
        }
        AppTheme.MIDNIGHT -> {
            midnightColors
        }
    }
}

private val lightColors = darkColors(
    primary = Color(0xFFE0E0E0),
    onPrimary = Color.Black,
    primaryVariant = Color.Black,
    secondary = Color(0xFFFF4081),
    onSecondary = Color.White,
    surface = Color(0xFFF5F5F5),
    onSurface = Color.Black,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
)

private val darkColors = darkColors(
    primary = Color.DarkGray,
    onPrimary = Color.White,
    primaryVariant = Colors.ContentBackground,
    secondary = Color(0xFFFF4081),
    onSecondary = Color.White,
    surface = Color(0xFF37474F),
    onSurface = Color.White,
    background = Colors.ContentBackground,
    onBackground = Color.White
)

private val midnightColors = darkColors(
    primary = Color.DarkGray,
    onPrimary = Color.White,
    primaryVariant = Color.Black,
    secondary = Color(0xFFFF4081),
    onSecondary = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    background = Color.Black,
    onBackground = Color.White
)

private class SunsetRippleTheme(
    val appTheme: AppTheme
) : RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        return MaterialTheme.colors.onSurface
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleTheme.defaultRippleAlpha(
            Color.Black,
            lightTheme = appTheme.isLight
        )
    }
}