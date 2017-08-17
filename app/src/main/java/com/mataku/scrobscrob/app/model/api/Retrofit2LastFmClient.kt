package com.mataku.scrobscrob.app.model.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mataku.scrobscrob.app.model.api.service.LastFmService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit2LastFmClient {
    companion object {
        fun createService(): LastFmService {
            val apiUrl = "https://ws.audioscrobbler.com/"
            val gson = GsonBuilder().setLenient().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(LastFmService::class.java)
        }
    }
}