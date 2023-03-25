package com.mataku.scrobscrob.data.api.di

import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import org.koin.dsl.module

val apiModule = module {
  single {
    LastFmService(LastFmHttpClient.create())
  }
}
