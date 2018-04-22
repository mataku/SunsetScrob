package com.mataku.scrobscrob.app.model.api.okhttp3

import com.mataku.scrobscrob.app.util.AppUtil
import okhttp3.Interceptor
import okhttp3.Response

class LastFmApiAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val appUtil = AppUtil()

        val url = chain.request().url().newBuilder()
                .addQueryParameter("api_key", appUtil.apiKey)
                .build()
        val request = chain.request().newBuilder()
                .url(url)
                .build()
        return chain.proceed(request)
    }
}