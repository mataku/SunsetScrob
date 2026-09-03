package com.mataku.scrobscrob.auth.webauth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.net.toUri
import dev.zacsweers.metro.Inject

@Inject
class CustomTabsWebAuthLauncher : LastFmWebAuthLauncher {

  @Composable
  override fun rememberLaunch(onResult: (LastFmWebAuthResult) -> Unit): (String) -> Unit {
    val containerHeightPx = LocalWindowInfo.current.containerSize.height
    val currentOnResult by rememberUpdatedState(onResult)
    val activityLauncher = rememberLauncherForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) {
      currentOnResult(LastFmWebAuthResult.Closed)
    }
    return remember(activityLauncher, containerHeightPx) {
      { url ->
        runCatching {
          val intent = WebAuthCustomTabs.intent(WebAuthCustomTabs.sheetHeightPx(containerHeightPx)).intent
          intent.data = url.toUri()
          activityLauncher.launch(intent)
        }.onFailure {
          currentOnResult(LastFmWebAuthResult.Failed)
        }
      }
    }
  }
}
