package com.mataku.scrobscrob.ui_common.style

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

actual val notoSansJpFontFamily: FontFamily = FontFamily(
  Font("font/notosansjp_regular.ttf", FontWeight.Normal),
  Font("font/notosansjp_bold.ttf", FontWeight.Bold),
  Font("font/notosansjp_medium.ttf", FontWeight.Medium),
)

actual fun noFontPaddingPlatformTextStyle(): PlatformTextStyle? = null
