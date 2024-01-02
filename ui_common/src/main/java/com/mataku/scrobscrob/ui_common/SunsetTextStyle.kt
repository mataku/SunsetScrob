package com.mataku.scrobscrob.ui_common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography

object SunsetTextStyle {
  private val notoSansJpFamily = FontFamily(
    Font(R.font.notosansjp_regular, FontWeight.Normal),
    Font(R.font.notosansjp_bold, FontWeight.Bold),
    Font(R.font.notosansjp_medium, FontWeight.Medium),
  )

  // TODO: font
  val caption
    @Composable
    get() = TextStyle(
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onSecondary,
      fontFamily = notoSansJpFamily,
      platformStyle = PlatformTextStyle(
        includeFontPadding = false
      )
    )

  @ShowkaseTypography(name = "body1")
  val body1 = TextStyle(
    fontSize = 16.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = PlatformTextStyle(
      includeFontPadding = false
    )
  )

  @ShowkaseTypography(name = "body2")
  val body2 = TextStyle(
    fontSize = 14.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = PlatformTextStyle(
      includeFontPadding = false
    )
  )

  val button
    @Composable get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp,
      letterSpacing = 1.25.sp,
      color = MaterialTheme.colorScheme.onSecondary,
      fontFamily = notoSansJpFamily,
      platformStyle = PlatformTextStyle(
        includeFontPadding = false
      )
    )

  @ShowkaseTypography(name = "header title")
  val h6 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = PlatformTextStyle(
      includeFontPadding = false
    )
  )

  val h7 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    letterSpacing = 0.15.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = PlatformTextStyle(
      includeFontPadding = false
    )
  )

  @ShowkaseTypography(name = "subtitle")
  val subtitle1 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp,
    fontFamily = notoSansJpFamily,
    platformStyle = PlatformTextStyle(
      includeFontPadding = false
    )
  )
}
