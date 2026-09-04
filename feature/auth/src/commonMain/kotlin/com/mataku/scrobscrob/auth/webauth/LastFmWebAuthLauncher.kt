package com.mataku.scrobscrob.auth.webauth

import androidx.compose.runtime.Composable

interface LastFmWebAuthLauncher {
  @Composable
  fun rememberLaunch(onResult: (LastFmWebAuthResult) -> Unit): (String) -> Unit
}
