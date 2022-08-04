package com.mataku.scrobscrob.app

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.mataku.scrobscrob.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class App : Application() {
  override fun onCreate() {
    super.onCreate()
    val imageLoaderBuilder = ImageLoader.Builder(applicationContext)
      .memoryCachePolicy(CachePolicy.DISABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .networkCachePolicy(CachePolicy.ENABLED)
      .diskCache {
        DiskCache.Builder()
          .directory(cacheDir.resolve("sunsetscrob_image"))
          .maxSizeBytes(256L * 1024L * 1024L)
          .build()
      }.crossfade(true)

    if (BuildConfig.DEBUG) {
      imageLoaderBuilder.logger(DebugLogger())
    }
    Coil.setImageLoader(imageLoaderBuilder.build())
  }
}
