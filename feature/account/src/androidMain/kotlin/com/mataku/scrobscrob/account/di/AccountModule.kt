package com.mataku.scrobscrob.account.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.mataku.scrobscrob.account.permission.AndroidNotificationListenerPermission
import com.mataku.scrobscrob.account.permission.NotificationListenerPermission
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.account.update.PlayInAppUpdateManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
interface AccountModule {
  @Binds
  fun provideInAppUpdateManager(manager: PlayInAppUpdateManager): InAppUpdateManager

  @Binds
  fun provideNotificationListenerPermission(
    permission: AndroidNotificationListenerPermission
  ): NotificationListenerPermission

  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideAppUpdateManager(context: Context): AppUpdateManager {
      return AppUpdateManagerFactory.create(context)
    }
  }
}
