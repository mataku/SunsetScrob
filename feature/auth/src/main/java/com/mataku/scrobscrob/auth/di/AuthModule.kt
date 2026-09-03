package com.mataku.scrobscrob.auth.di

import com.mataku.scrobscrob.auth.webauth.CustomTabsWebAuthLauncher
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface AuthModule {

  @Binds
  fun provideWebAuthLauncher(launcher: CustomTabsWebAuthLauncher): LastFmWebAuthLauncher
}
