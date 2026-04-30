package com.mataku.scrobscrob.data.api.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.mataku.scrobscrob.data.api.BuildConfig
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.LastFmServiceImpl
import com.mataku.scrobscrob.data.api.okhttp.LastfmApiAuthInterceptor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toOkioPath
import java.io.File

@ContributesTo(AppScope::class)
interface ApiModule {

  @Binds
  fun bindLastFmService(impl: LastFmServiceImpl): LastFmService

  companion object {

    @SingleIn(AppScope::class)
    @Provides
    fun provideOkhttpClient(context: Context): OkHttpClient {
      val builder = OkHttpClient.Builder()
        .cache(
          Cache(
            directory = File(context.cacheDir, "sunsetscrob_cache"),
            maxSize = 512L * 1024L * 1024L,
          ),
        )
      if (BuildConfig.DEBUG) {
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
      }
      return builder.build()
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(okHttpClient: OkHttpClient): HttpClientEngine =
      OkHttp.create {
        preconfigured = okHttpClient.newBuilder()
          .addInterceptor(LastfmApiAuthInterceptor())
          .build()
      }

    @SingleIn(AppScope::class)
    @Provides
    fun provideImageLoader(
      context: Context,
      okHttpClient: OkHttpClient,
    ): ImageLoader =
      ImageLoader.Builder(context)
        .components {
          add(
            OkHttpNetworkFetcherFactory(
              callFactory = { okHttpClient.newBuilder().build() },
            ),
          )
        }
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .diskCache {
          DiskCache.Builder()
            .directory(context.cacheDir.resolve("sunsetscrob_image").toOkioPath())
            .maxSizeBytes(1073741824L)
            .build()
        }
        .build()
  }
}
