package com.mataku.scrobscrob.app.testing.di

import android.content.Context
import coil3.ColorImage
import coil3.ImageLoader
import coil3.test.FakeImageLoaderEngine
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface FakeImageLoaderModule {
  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideImageLoader(context: Context): ImageLoader {
      val engine = FakeImageLoaderEngine.Builder()
        .default(ColorImage(0xFF3A3A3A.toInt()))
        .build()
      return ImageLoader.Builder(context)
        .components { add(engine) }
        .build()
    }
  }
}
