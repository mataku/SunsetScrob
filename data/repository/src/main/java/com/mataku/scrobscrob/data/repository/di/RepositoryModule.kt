package com.mataku.scrobscrob.data.repository.di

import com.mataku.scrobscrob.data.api.di.apiModule
import com.mataku.scrobscrob.data.db.di.databaseModule
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.data.repository.ArtistRepositoryImpl
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepositoryImpl
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepositoryImpl
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.SessionRepositoryImpl
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.ThemeRepositoryImpl
import com.mataku.scrobscrob.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.data.repository.TopAlbumsRepositoryImpl
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.TopArtistsRepositoryImpl
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.data.repository.TrackRepositoryImpl
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.data.repository.UsernameRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
  includes(apiModule, databaseModule)
  single<SessionRepository> {
    SessionRepositoryImpl(
      get(),
      get(),
      get(),
      get()
    )
  }

  single<UsernameRepository> {
    UsernameRepositoryImpl(
      get()
    )
  }

  single<TopAlbumsRepository> {
    TopAlbumsRepositoryImpl(
      get()
    )
  }

  single<TopArtistsRepository> {
    TopArtistsRepositoryImpl(
      get()
    )
  }

  single<ScrobbleRepository> {
    ScrobbleRepositoryImpl(
      get(),
      get(),
      get(),
      get()
    )
  }

  single<ThemeRepository> {
    ThemeRepositoryImpl(
      get()
    )
  }

  single<TrackRepository> {
    TrackRepositoryImpl(
      get(),
      get()
    )
  }

  single<ArtistRepository> {
    ArtistRepositoryImpl(
      get()
    )
  }

  single<ScrobbleSettingRepository> {
    ScrobbleSettingRepositoryImpl(
      get()
    )
  }
}
