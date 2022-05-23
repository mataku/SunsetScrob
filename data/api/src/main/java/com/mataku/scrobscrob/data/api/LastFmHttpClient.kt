package com.mataku.scrobscrob.data.api

import com.mataku.scrobscrob.data.api.okhttp.LastfmApiAuthInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

object LastFmHttpClient {
    fun create(

    ): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                addInterceptor(LastfmApiAuthInterceptor())
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}