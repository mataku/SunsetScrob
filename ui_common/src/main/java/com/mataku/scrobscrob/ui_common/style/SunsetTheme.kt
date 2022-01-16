package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
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

private val darkColors = darkColors(
    primary = Color(0xff37474F),
    primaryVariant = Color(0xff000000),
    secondary = Color(0xffFF4081),
    onSecondary = Color.White
)