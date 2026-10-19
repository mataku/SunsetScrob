package com.mataku.scrobscrob.app.smoke

import android.app.Application
import android.content.Context
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.di.AppGraphContract
import com.mataku.scrobscrob.auth.di.AuthModule
import com.mataku.scrobscrob.core.AppBuildInfo
import com.mataku.scrobscrob.data.api.di.HttpEngineModule
import com.mataku.scrobscrob.data.api.di.ImageLoaderModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(
  scope = AppScope::class,
  additionalScopes = [SmokeScope::class],
  excludes = [HttpEngineModule::class, AuthModule::class, ImageLoaderModule::class]
)
internal interface SmokeAppGraph : AppGraphContract {

  @Provides
  fun provideApplicationContext(application: Application): Context = application

  @Provides
  fun provideAppBuildInfo(): AppBuildInfo = AppBuildInfo(isDebug = BuildConfig.DEBUG)

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides application: Application): SmokeAppGraph
  }
}
