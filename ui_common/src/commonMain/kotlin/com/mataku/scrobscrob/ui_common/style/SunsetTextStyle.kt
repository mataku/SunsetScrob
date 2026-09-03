package com.mataku.scrobscrob.ui_common.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

expect val notoSansJpFontFamily: FontFamily

expect fun noFontPaddingPlatformTextStyle(): PlatformTextStyle?

object SunsetTextStyle {
  private val notoSansJpFamily: FontFamily get() = notoSansJpFontFamily

  val caption
    @Composable
    get() = TextStyle(
      fontSize = 13.sp,
      color = LocalAppTheme.current.onSecondaryColor(),
      fontFamily = notoSansJpFamily,
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val body = TextStyle(
    fontSize = 16.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = noFontPaddingPlatformTextStyle()
  )

  val label = TextStyle(
    fontSize = 14.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = noFontPaddingPlatformTextStyle()
  )

  val button
    @Composable get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp,
      letterSpacing = 1.25.sp,
      color = LocalAppTheme.current.onSecondaryColor(),
      fontFamily = notoSansJpFamily,
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val title = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = noFontPaddingPlatformTextStyle(),
  )

  val headline = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    letterSpacing = 0.15.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = noFontPaddingPlatformTextStyle()
  )

  val subtitle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = noFontPaddingPlatformTextStyle()
  )
}
