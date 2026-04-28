package com.mataku.scrobscrob.app

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.di.AppGraph
import com.mataku.scrobscrob.app.di.AppGraphContract
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.data.repository.di.ScrobbleServiceDependencies
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication
import timber.log.Timber

open class App : Application(), MetroApplication, SingletonImageLoader.Factory, ScrobbleServiceDependencies {

  internal val appGraph: AppGraphContract by lazy { newAppGraph() }

  internal open fun newAppGraph(): AppGraphContract =
    createGraphFactory<AppGraph.Factory>().create(this)

  override val appComponentProviders: MetroAppComponentProviders
    get() = appGraph

  override val nowPlayingRepository: NowPlayingRepository get() = appGraph.nowPlayingRepository
  override val trackRepository: TrackRepository get() = appGraph.trackRepository
  override val scrobbleRepository: ScrobbleRepository get() = appGraph.scrobbleRepository
  override val scrobbleSettingRepository: ScrobbleSettingRepository get() = appGraph.scrobbleSettingRepository

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun newImageLoader(context: Context): ImageLoader {
    return appGraph.imageLoader
  }
}
