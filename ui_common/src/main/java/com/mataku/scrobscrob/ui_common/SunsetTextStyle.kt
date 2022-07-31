package com.mataku.scrobscrob.ui_common

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mataku.scrobscrob.ui_common.style.Colors

object SunsetTextStyle {
    // TODO: font
    val caption = TextStyle(
        fontSize = 12.sp,
        color = Colors.textSecondary
    )

    val body1 = TextStyle(
        fontSize = 16.sp
    )

    val body2 = TextStyle(
        fontSize = 14.sp
    )

    val h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    )

}