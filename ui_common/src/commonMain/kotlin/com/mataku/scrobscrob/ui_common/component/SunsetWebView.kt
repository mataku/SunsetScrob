package com.mataku.scrobscrob.ui_common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SunsetWebView(
  url: String,
  modifier: Modifier = Modifier,
  openLinksExternally: Boolean = false,
  onPageFinished: (title: String) -> Unit = {},
)
