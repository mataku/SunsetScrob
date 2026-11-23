package com.mataku.scrobscrob.account.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
interface AccountModule {
  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideAppUpdateManager(context: Context): AppUpdateManager {
      return AppUpdateManagerFactory.create(context)
    }
  }
}
