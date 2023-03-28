package com.mataku.scrobscrob.app

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.mataku.scrobscrob.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class App : Application() {
  override fun onCreate() {
    super.onCreate()
    val dir = cacheDir.resolve("sunsetscrob_image")

    val imageLoaderBuilder = ImageLoader.Builder(applicationContext)
      .memoryCachePolicy(CachePolicy.DISABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .networkCachePolicy(CachePolicy.ENABLED)
      .diskCache {
        DiskCache.Builder()
          .directory(dir)
          .maxSizeBytes(256L * 1024L * 1024L)
          .build()
      }.crossfade(300)

    if (BuildConfig.DEBUG) {
      imageLoaderBuilder.logger(DebugLogger())
    }
    Coil.setImageLoader(imageLoaderBuilder.build())
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
