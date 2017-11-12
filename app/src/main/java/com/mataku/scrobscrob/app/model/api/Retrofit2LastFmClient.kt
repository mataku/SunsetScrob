package com.mataku.scrobscrob.app.model.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mataku.scrobscrob.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit2LastFmClient {
    companion object {
        fun <T> create(service: Class<T>): T {
            val client = builderHttpClient()

            val apiUrl = "https://ws.audioscrobbler.com/"
            val gson = GsonBuilder().setLenient().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            return Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(client)
                    .build()
                    .create(service)
        }

        private fun builderHttpClient(): OkHttpClient {
            val client = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }

            return client.build()
        }
    }
}