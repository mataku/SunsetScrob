package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun SunsetWebView(
  url: String,
  modifier: Modifier,
  onPageFinished: (title: String) -> Unit,
  openLinksExternally: Boolean,
) {
  Box(modifier = modifier)
}
