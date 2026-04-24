package com.mataku.scrobscrob.app.di

import android.content.Context
import android.content.Intent
import com.airbnb.android.showkase.models.Showkase
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.app.ui.getBrowserIntent
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(AppScope::class)
class AppInfoProviderImpl : AppInfoProvider {
  override fun appVersion(): String = BuildConfig.VERSION_NAME

  override fun navigateToUiCatalogIntent(context: Context) {
    val intent = Showkase.getBrowserIntent(context)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
  }
}
