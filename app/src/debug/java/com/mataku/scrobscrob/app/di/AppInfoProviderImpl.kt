package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.account.AppInfoProvider
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(AppScope::class)
class AppInfoProviderImpl : AppInfoProvider {
  override fun appVersion(): String = BuildConfig.VERSION_NAME
}
