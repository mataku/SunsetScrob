package com.mataku.scrobscrob.ui_common.style

import androidx.compose.ui.text.PlatformTextStyle

actual fun noFontPaddingPlatformTextStyle(): PlatformTextStyle? =
  PlatformTextStyle(includeFontPadding = false)
