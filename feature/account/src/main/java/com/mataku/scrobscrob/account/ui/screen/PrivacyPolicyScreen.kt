package com.mataku.scrobscrob.account.ui.screen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.ui_common.organism.ContentHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrivacyPolicyScreen() {
    LazyColumn(content = {
        stickyHeader {
            ContentHeader(text = stringResource(id = R.string.item_privacy_policy))
        }
        item {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = {
                        WebView(it)
                    },
                    update = { webView ->
                        webView.webViewClient = WebViewClient()
                        webView.loadUrl("https://mataku.github.io/sunsetscrob/index.html")
                    }
                )
            }
        }
    }, modifier = Modifier.fillMaxSize())
}