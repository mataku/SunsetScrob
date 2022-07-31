package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
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
    MaterialTheme(
        colors = theme.colors(),
        content = content
    )
}

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