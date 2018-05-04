package com.mataku.scrobscrob.app.model.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.api.okhttp3.LastFmApiAuthInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class LastFmApiClient {
    companion object {
        fun <T> create(service: Class<T>): T {
            val client = httpClientBuilder()

            val apiUrl = "https://ws.audioscrobbler.com/"
            val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()
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