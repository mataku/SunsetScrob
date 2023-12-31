package com.mataku.scrobscrob.data.repository.di

import com.mataku.scrobscrob.data.api.di.ApiModule
import com.mataku.scrobscrob.data.db.di.DatabaseModule
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.data.repository.AlbumRepositoryImpl
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.data.repository.ArtistRepositoryImpl
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.NowPlayingRepositoryImpl
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepositoryImpl
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepositoryImpl
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.SessionRepositoryImpl
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.ThemeRepositoryImpl
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.TopArtistsRepositoryImpl
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.data.repository.TrackRepositoryImpl
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.data.repository.UsernameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [ApiModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Singleton
  @Binds
  abstract fun provideSessionRepository(repository: SessionRepositoryImpl): SessionRepository

  @Singleton
  @Binds
  abstract fun provideUsernameRepository(repository: UsernameRepositoryImpl): UsernameRepository

  @Singleton
  @Binds
  abstract fun provideTopAlbumsRepository(repository: AlbumRepositoryImpl): AlbumRepository

  @Singleton
  @Binds
  abstract fun provideTopArtistsRepository(repository: TopArtistsRepositoryImpl): TopArtistsRepository

  @Singleton
  @Binds
  abstract fun provideScrobbleRepository(repository: ScrobbleRepositoryImpl): ScrobbleRepository

  @Singleton
  @Binds
  abstract fun provideThemeRepository(repository: ThemeRepositoryImpl): ThemeRepository

  @Singleton
  @Binds
  abstract fun provideTrackRepository(repository: TrackRepositoryImpl): TrackRepository

  @Singleton
  @Binds
  abstract fun provideArtistRepository(repository: ArtistRepositoryImpl): ArtistRepository

  @Singleton
  @Binds
  abstract fun provideNowPlayingRepository(repository: NowPlayingRepositoryImpl): NowPlayingRepository

  @Singleton
  @Binds
  abstract fun provideScrobbleSettingRepository(repository: ScrobbleSettingRepositoryImpl): ScrobbleSettingRepository
}
