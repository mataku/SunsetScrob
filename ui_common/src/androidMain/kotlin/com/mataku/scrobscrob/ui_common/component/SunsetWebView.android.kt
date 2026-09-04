package com.mataku.scrobscrob.ui_common.component

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun SunsetWebView(
  url: String,
  modifier: Modifier,
  openLinksExternally: Boolean,
  onPageFinished: (title: String) -> Unit,
) {
  val uriHandler = LocalUriHandler.current
  val currentOnPageFinished by rememberUpdatedState(onPageFinished)
  AndroidView(
    factory = { context ->
      WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
        )
        webViewClient = object : WebViewClient() {
          override fun onPageFinished(view: WebView?, url: String?) {
            currentOnPageFinished(view?.title ?: "")
          }

          override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
          ): Boolean {
            if (!openLinksExternally) return false
            val target = request?.url?.toString() ?: return false
            runCatching { uriHandler.openUri(target) }
            return true
          }
        }
      }
    },
    update = { webView ->
      if (webView.tag != url) {
        webView.tag = url
        webView.loadUrl(url)
      }
    },
    modifier = modifier
  )
}
