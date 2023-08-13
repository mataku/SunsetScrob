package com.mataku.scrobscrob.app.di

import android.content.Context
import android.content.Intent
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.account.AppInfoProvider
import java.lang.IllegalStateException
import javax.inject.Inject

class AppInfoProviderImpl @Inject constructor() : AppInfoProvider {
  override fun appVersion(): String = BuildConfig.VERSION_NAME

  override fun navigateToUiCatalogIntent(context: Context) {
    throw IllegalStateException("Unexpected uiCatalogIntent call on release build")
  }
}
