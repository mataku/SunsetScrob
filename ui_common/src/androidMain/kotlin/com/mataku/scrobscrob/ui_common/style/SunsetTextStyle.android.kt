package com.mataku.scrobscrob.ui_common.style

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mataku.scrobscrob.ui_common.R

actual val notoSansJpFontFamily: FontFamily = FontFamily(
  Font(R.font.notosansjp_regular, FontWeight.Normal),
  Font(R.font.notosansjp_bold, FontWeight.Bold),
  Font(R.font.notosansjp_medium, FontWeight.Medium),
)

actual fun noFontPaddingPlatformTextStyle(): PlatformTextStyle? =
  PlatformTextStyle(includeFontPadding = false)
