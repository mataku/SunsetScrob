package com.mataku.scrobscrob.app.model.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.api.okhttp3.LastFmApiAuthInterceptor
import com.mataku.scrobscrob.app.util.AppUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class LastFmApiClient {

    companion object {
        private val appUtil = AppUtil()

        val client: HttpClient
            get() {
                val config: HttpClientConfig<*>.() -> Unit = {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(Json.nonstrict)
                    }
                    defaultRequest {
                        parameter("api_key", appUtil.apiKey)
                    }
                    if (BuildConfig.DEBUG) {
                        install(Logging) {
                            logger = Logger.SIMPLE
                            level = LogLevel.ALL
                        }
                    }
                }
                return HttpClient(Android).config(config)
            }

        fun <T> create(service: Class<T>): T {
            val client = httpClientBuilder()

            val apiUrl = "https://ws.audioscrobbler.com/"
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            return Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
                .create(service)
        }

        private fun httpClientBuilder(): OkHttpClient {
            val client = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }
            client.addInterceptor(LastFmApiAuthInterceptor())

            return client.build()
        }
    }
}