package com.mataku.scrobscrob.data.api.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.mataku.scrobscrob.data.api.BuildConfig
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.okhttp.LastfmApiAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toOkioPath
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

  @Singleton
  @Provides
  fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
    val okhttpClientBuilder = OkHttpClient.Builder()
      .cache(
        Cache(
          directory = File(context.cacheDir, "sunsetscrob_cache"),
          maxSize = 512L * 1024L * 1024L // 512MB
        )
      )

    if (BuildConfig.DEBUG) {
      okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

    return okhttpClientBuilder.build()
  }

  @Singleton
  @Provides
  fun provideLastFmService(
    okHttpClient: OkHttpClient
  ): LastFmService {
    val okHttpEngine = OkHttp.create {
      preconfigured = okHttpClient.newBuilder()
        .addInterceptor(LastfmApiAuthInterceptor())
        .build()
    }
    return LastFmService(
      LastFmHttpClient.create(okHttpEngine)
    )
  }

  @Singleton
  @Provides
  fun provideImageLoader(
    @ApplicationContext context: Context,
    okHttpClient: OkHttpClient
  ): ImageLoader {
    return ImageLoader.Builder(context)
      .components {
        add(
          OkHttpNetworkFetcherFactory(
            callFactory = {
              okHttpClient.newBuilder().build()
            }
          )
        )
      }
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.DISABLED)
      .diskCache {
        DiskCache.Builder()
          .directory(context.cacheDir.resolve("sunsetscrob_image").toOkioPath())
          .maxSizeBytes(1073741824L) // 1GB
          .build()
      }
      .build()
  }
}
