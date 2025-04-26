package com.mataku.scrobscrob.account.ui.screen

import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrivacyPolicyScreen(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  LazyColumn(
    content = {
      item {
        Column(modifier = Modifier.fillMaxSize()) {
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
              webView.loadUrl("https://mataku.github.io/sunsetscrob/index.html")
            }
          )
        }
      }
    },
    modifier = modifier.fillMaxSize()
  )

//  val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
//  ModalBottomSheetLayout(
//    sheetContent = {
//      WebViewScreen(
//        url = "https://mataku.github.io/sunsetscrob/index.html",
//        modifier = Modifier.height(600.dp)
//      )
//    },
//    sheetState = sheetState,
//    scrimColor = Color.Transparent
//  ) {
//  }
}
