package com.mataku.scrobscrob.data.api.di

import android.content.Context
import com.mataku.scrobscrob.data.api.BuildConfig
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

  @Singleton
  @Provides
  fun provideLastFmService(@ApplicationContext context: Context): LastFmService {
    return LastFmService(provideHttpClient(context))
  }

  private fun provideHttpClient(@ApplicationContext context: Context): HttpClient {
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

    val okHttpEngine = OkHttp.create {
      preconfigured = okhttpClientBuilder.build()
    }
    return LastFmHttpClient.create(okHttpEngine)
  }
}
