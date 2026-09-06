package com.mataku.scrobscrob.data.api.okhttp

import com.mataku.scrobscrob.data.api.LastFmApiCredentials
import okhttp3.Interceptor
import okhttp3.Response

class LastfmApiAuthInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val url = chain.request().url.newBuilder()
      .addQueryParameter("api_key", LastFmApiCredentials.API_KEY)
      .build()

    val request = chain.request().newBuilder()
      .url(url)
      .build()

    return chain.proceed(request)
  }
}
