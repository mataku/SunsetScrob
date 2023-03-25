package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.account.AppInfoProvider

class AppInfoProviderImpl() : AppInfoProvider {
  override fun appVersion(): String = BuildConfig.VERSION_NAME
}
