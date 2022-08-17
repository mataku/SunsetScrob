package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.account.AppInfoProvider
import javax.inject.Inject

class AppInfoProviderImpl @Inject constructor() : AppInfoProvider {
  override fun appVersion(): String = BuildConfig.VERSION_NAME
}
