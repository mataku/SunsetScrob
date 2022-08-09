package com.mataku.scrobscrob.ui_common.template

import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String, modifier: Modifier) {
  val context = LocalContext.current
  Column(
    modifier = modifier
      .verticalScroll(rememberScrollState())
  ) {
    AndroidView(
      factory = {
        WebView(it)
      },
      update = { webView ->
        webView.webViewClient = object : WebViewClient() {
          override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
          ): Boolean {
            val urlStr = request?.url
            urlStr?.let {
              kotlin.runCatching {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.toString())))
              }.fold(
                onSuccess = {},
                onFailure = {}
              )
              return true
            }

            return false
          }
        }
        webView.loadUrl(url)
      }
    )
  }
}
