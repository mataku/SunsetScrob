package com.mataku.scrobscrob.ui_common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SunsetTextStyle {
  // TODO: font
  val caption
    @Composable get() = TextStyle(
      fontSize = 13.sp,
      color = MaterialTheme.colors.onSecondary
    )

  val body1 = TextStyle(
    fontSize = 16.sp
  )

  val body2 = TextStyle(
    fontSize = 14.sp
  )

  val button
    @Composable get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp,
      letterSpacing = 1.25.sp,
      color = MaterialTheme.colors.onSecondary
    )

  val h6 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
  )

  val subtitle1 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
  )

}
