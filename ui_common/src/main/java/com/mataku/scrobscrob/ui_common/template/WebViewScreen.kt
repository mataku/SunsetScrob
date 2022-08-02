package com.mataku.scrobscrob.ui_common.template

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String, modifier: Modifier) {
  Column(
    modifier = modifier
      .verticalScroll(rememberScrollState())
  ) {
    AndroidView(
      factory = {
        WebView(it)
      },
      update = { webView ->
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
      }
    )
  }
}
