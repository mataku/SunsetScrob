package com.mataku.scrobscrob.ui_common.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mataku.scrobscrob.ui_common.generated.resources.Res
import com.mataku.scrobscrob.ui_common.generated.resources.notosansjp_bold
import com.mataku.scrobscrob.ui_common.generated.resources.notosansjp_medium
import com.mataku.scrobscrob.ui_common.generated.resources.notosansjp_regular
import org.jetbrains.compose.resources.Font

expect fun noFontPaddingPlatformTextStyle(): PlatformTextStyle?

@Composable
fun notoSansJpFontFamily(): FontFamily = FontFamily(
  Font(Res.font.notosansjp_regular, FontWeight.Normal),
  Font(Res.font.notosansjp_bold, FontWeight.Bold),
  Font(Res.font.notosansjp_medium, FontWeight.Medium),
)

object SunsetTextStyle {
  val caption
    @Composable
    get() = TextStyle(
      fontSize = 13.sp,
      color = LocalAppTheme.current.onSecondaryColor(),
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val body
    @Composable
    get() = TextStyle(
      fontSize = 16.sp,
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val label
    @Composable
    get() = TextStyle(
      fontSize = 14.sp,
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val button
    @Composable
    get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp,
      letterSpacing = 1.25.sp,
      color = LocalAppTheme.current.onSecondaryColor(),
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val title
    @Composable
    get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 20.sp,
      letterSpacing = 0.15.sp,
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle(),
    )

  val headline
    @Composable
    get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 18.sp,
      letterSpacing = 0.15.sp,
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle()
    )

  val subtitle
    @Composable
    get() = TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize = 16.sp,
      letterSpacing = 0.15.sp,
      fontFamily = notoSansJpFontFamily(),
      platformStyle = noFontPaddingPlatformTextStyle()
    )
}
