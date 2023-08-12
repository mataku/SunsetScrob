package com.mataku.scrobscrob.ui_common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography

object SunsetTextStyle {
  // TODO: font
  @ShowkaseTypography(name = "caption")
  val caption = TextStyle(
    fontSize = 13.sp,
  )

  @ShowkaseTypography(name = "body1")
  val body1 = TextStyle(
    fontSize = 16.sp
  )

  @ShowkaseTypography(name = "body2")
  val body2 = TextStyle(
    fontSize = 14.sp
  )

  val button
    @Composable get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp,
      letterSpacing = 1.25.sp,
      color = MaterialTheme.colorScheme.onSecondary
    )

  @ShowkaseTypography(name = "header title")
  val h6 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
  )

  val h7 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    letterSpacing = 0.15.sp
  )

  @ShowkaseTypography(name = "subtitle")
  val subtitle1 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
  )
}
