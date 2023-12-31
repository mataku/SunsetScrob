package com.mataku.scrobscrob.data.api.di

import com.mataku.scrobscrob.data.api.BuildConfig
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.okhttp.LastfmApiAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

  @Singleton
  @Provides
  fun provideLastFmService(): LastFmService {
    return LastFmService(provideHttpClient())
  }
  
  private fun provideHttpClient(): HttpClient {
    val okHttpEngine = OkHttp.create {
      addInterceptor(LastfmApiAuthInterceptor())
      if (BuildConfig.DEBUG) {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
      }
    }
    return LastFmHttpClient.create(okHttpEngine)
  }
}
