package com.mataku.scrobscrob.ui_common.template

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.ui_common.organism.NavigationHeader
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.coroutines.launch

@Composable
fun WebViewScreen(
  url: String,
  navController: NavController,
  modifier: Modifier
) {
  var title by remember {
    mutableStateOf("")
  }
  var visibility by remember {
    mutableStateOf(false)
  }
  val visibilityValue = remember {
    Animatable(0F)
  }
  val screenHeight = LocalConfiguration.current.screenHeightDp

  val coroutineScope = rememberCoroutineScope()

  Column(
    modifier = modifier
      .fillMaxSize()
  ) {
    NavigationHeader(text = title) {
      navController.popBackStack()
    }
    // NOTE: specified height and visibility animation as workaround for compose WebView flickering
    AndroidView(
      factory = {
        WebView(it)
      },
      update = { webView ->
        webView.webViewClient = object : WebViewClient() {
          override fun onPageFinished(view: WebView?, url: String?) {
            title = view?.title ?: ""
            if (!visibility) {
              coroutineScope.launch {
                visibilityValue.animateTo(1F, tween(1000))
              }
              visibility = true
            }
          }
        }
        webView.loadUrl(url)
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(screenHeight.dp - 64.dp)
        .alpha(visibilityValue.value)
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun WebViewScreenPreview() {
  SunsetThemePreview {
    Surface {
      WebViewScreen(
        url = "https://www.google.com",
        navController = rememberNavController(),
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
