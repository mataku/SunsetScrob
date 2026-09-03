package com.mataku.scrobscrob.auth.webauth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.browser.auth.AuthTabIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.core.net.toUri
import dev.zacsweers.metro.Inject

@Inject
class AuthTabWebAuthLauncher : LastFmWebAuthLauncher {

  @Composable
  override fun rememberLaunch(onResult: (LastFmWebAuthResult) -> Unit): (String) -> Unit {
    val currentOnResult by rememberUpdatedState(onResult)
    val activityLauncher = rememberLauncherForActivityResult(
      AuthTabIntent.AuthenticateUserResultContract()
    ) { result ->
      currentOnResult(mapAuthTabResult(result.resultCode, result.resultUri?.toString()))
    }
    return remember(activityLauncher) {
      { url ->
        runCatching {
          AuthTabIntent.Builder()
            .build()
            .launch(
              activityLauncher,
              url.toUri(),
              LastFmWebAuth.CALLBACK_HOST,
              LastFmWebAuth.CALLBACK_PATH,
            )
        }.onFailure {
          currentOnResult(LastFmWebAuthResult.Failed)
        }
      }
    }
  }
}
