package com.mataku.scrobscrob.data.api.di

import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

  @Singleton
  @Provides
  fun provideLastFmService(): LastFmService {
    return LastFmService(provideHttpClient())
  }

  private fun provideHttpClient(): HttpClient {
    return LastFmHttpClient.create()
  }
}

val apiModule = module {
  single {
    LastFmService(LastFmHttpClient.create())
  }
}
