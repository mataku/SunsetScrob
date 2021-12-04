package com.mataku.scrobscrob.data.api.di

import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    internal fun provideHttpClient(): HttpClient {
        return LastFmHttpClient.create()
    }

    @Singleton
    @Provides
    internal fun provideLastFmService(
        httpClient: HttpClient
    ): LastFmService {
        return LastFmService(httpClient)
    }
}