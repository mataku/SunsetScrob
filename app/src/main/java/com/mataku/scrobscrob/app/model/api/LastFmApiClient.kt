package com.mataku.scrobscrob.app.model.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.api.okhttp3.LastFmApiAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LastFmApiClient {
    companion object {
        fun <T> create(service: Class<T>): T {
            val client = httpClientBuilder()

            val apiUrl = "https://ws.audioscrobbler.com/"
            val gson = GsonBuilder().setLenient().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            return Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
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