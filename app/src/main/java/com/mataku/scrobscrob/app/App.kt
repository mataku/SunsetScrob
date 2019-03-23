package com.mataku.scrobscrob.app

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.app.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.app.model.api.okhttp3.LastFmApiAuthInterceptor
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import com.mataku.scrobscrob.app.model.api.service.UserTopArtistsService
import com.mataku.scrobscrob.app.ui.top.TopViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

open class App : Application() {

    private val appModules = module {
        single { okHttpClient() }
        single { apiClient(get()) }
        single { apiClient(get()).create(UserTopAlbumsService::class.java) }
        single { apiClient(get()).create(UserTopArtistsService::class.java) }
        single { TopArtistsRepository(get()) }
        single { TopAlbumsRepository(get()) }
        viewModel { TopViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(
            this,
            listOf(appModules)
        )
    }

    fun okHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }
        client.addInterceptor(LastFmApiAuthInterceptor())

        return client.build()
    }

    fun apiClient(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }
}