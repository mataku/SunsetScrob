package com.mataku.scrobscrob.app

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.mataku.scrobscrob.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
open class App : Application(), SingletonImageLoader.Factory {

  @Inject
  lateinit var imageLoader: ImageLoader

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun newImageLoader(context: Context): ImageLoader {
    return imageLoader
  }
}
