package com.mataku.scrobscrob.app.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.api.ApplicationJsonAdapterFactory
import com.mataku.scrobscrob.app.model.api.okhttp3.LastFmApiAuthInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkModule {

    fun <T> create(service: Class<T>): T {
        val client = httpClientBuilder()

        val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()
        return Retrofit.Builder()
            .baseUrl(API_URL)
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

    companion object {
        const val API_URL = "https://ws.audioscrobbler.com/"
    }

}