package com.mataku.scrobscrob.app

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.account.di.accountModule
import com.mataku.scrobscrob.app.di.appModule
import com.mataku.scrobscrob.data.repository.di.repositoryModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@HiltAndroidApp
open class App : Application() {
  override fun onCreate() {
    super.onCreate()
    // Cleanup coil cache dir
    val dir = cacheDir.resolve("sunsetscrob_image")
    kotlin.runCatching {
      if (dir.exists()) {
        dir.delete()
      }
    }
    val imageLoaderBuilder = ImageLoader.Builder(applicationContext)
      .memoryCachePolicy(CachePolicy.DISABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .networkCachePolicy(CachePolicy.ENABLED)
      .diskCache {
        DiskCache.Builder()
          .directory(dir)
          .maxSizeBytes(256L * 1024L * 1024L)
          .build()
      }.crossfade(true)

    if (BuildConfig.DEBUG) {
      imageLoaderBuilder.logger(DebugLogger())
    }
    Coil.setImageLoader(imageLoaderBuilder.build())
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    startKoin {
      androidContext(this@App)
      modules(appModule, repositoryModule, accountModule)
    }
  }
}
