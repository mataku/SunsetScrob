package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SunsetTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = darkColors,
        content = content
    )
}

private val lightColors = lightColors(
    
)

private val darkColors = darkColors(
    primary = Color(0xff4c626d),
    onPrimary = Color.White,
    primaryVariant = Colors.ContentBackground,
    secondary = Color(0xffFF4081),
    onSecondary = Color.White,
    surface = Colors.ContentBackground,
    onSurface = Color.White,
    background = Colors.ContentBackground,
    onBackground = Color.White
)