package com.mataku.scrobscrob.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.mataku.scrobscrob.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import okio.Path.Companion.toOkioPath
import timber.log.Timber

@HiltAndroidApp
open class App : Application(), SingletonImageLoader.Factory {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun newImageLoader(context: PlatformContext): ImageLoader {
    val dir = cacheDir.resolve("sunsetscrob_image")
    val imageLoaderBuilder = ImageLoader.Builder(applicationContext)
      .memoryCachePolicy(CachePolicy.DISABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .networkCachePolicy(CachePolicy.ENABLED)
      .diskCache {
        DiskCache.Builder()
          .directory(dir.toOkioPath())
          .maxSizeBytes(256L * 1024L * 1024L)
          .build()
      }.crossfade(300)

    if (BuildConfig.DEBUG) {
      imageLoaderBuilder.logger(DebugLogger())
    }

    return imageLoaderBuilder.build()
  }
}
