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
      currentOnResult(result.toWebAuthResult())
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

  private fun AuthTabIntent.AuthResult.toWebAuthResult(): LastFmWebAuthResult {
    if (resultCode == AuthTabIntent.RESULT_CANCELED) return LastFmWebAuthResult.Canceled
    if (resultCode != AuthTabIntent.RESULT_OK) return LastFmWebAuthResult.Failed
    val token = resultUri?.toString()?.let(LastFmWebAuth::tokenFromCallback)
    return if (token == null) LastFmWebAuthResult.Failed else LastFmWebAuthResult.Success(token)
  }
}
