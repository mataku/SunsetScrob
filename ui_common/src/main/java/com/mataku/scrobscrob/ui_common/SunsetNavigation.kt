package com.mataku.scrobscrob.ui_common

import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.screen.WebViewScreen

fun SunsetNavBuilder.commonGraph() {
  destination<WebViewKey> { key ->
    WebViewScreen(
      url = key.url,
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
}
