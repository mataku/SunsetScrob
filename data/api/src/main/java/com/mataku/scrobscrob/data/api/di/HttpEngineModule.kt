package com.mataku.scrobscrob.data.api.di

import com.mataku.scrobscrob.data.api.okhttp.LastfmApiAuthInterceptor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient

@ContributesTo(AppScope::class)
interface HttpEngineModule {
  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(okHttpClient: OkHttpClient): HttpClientEngine =
      OkHttp.create {
        preconfigured = okHttpClient.newBuilder()
          .addInterceptor(LastfmApiAuthInterceptor())
          .build()
      }
  }
}
