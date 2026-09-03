package com.mataku.scrobscrob.app.testing.di

import androidx.compose.runtime.Composable
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthResult
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

const val E2E_TOKEN = "e2e_token"

class FakeWebAuthLauncher : LastFmWebAuthLauncher {
  @Composable
  override fun rememberLaunch(onResult: (LastFmWebAuthResult) -> Unit): (String) -> Unit =
    { onResult(LastFmWebAuthResult.Success(E2E_TOKEN)) }
}

@ContributesTo(AppScope::class)
interface FakeWebAuthModule {
  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideWebAuthLauncher(): LastFmWebAuthLauncher = FakeWebAuthLauncher()
  }
}
