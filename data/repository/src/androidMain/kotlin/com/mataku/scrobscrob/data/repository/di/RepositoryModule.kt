package com.mataku.scrobscrob.data.repository.di

import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.data.repository.AlbumRepositoryImpl
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.data.repository.ArtistRepositoryImpl
import com.mataku.scrobscrob.data.repository.ArtworkRepository
import com.mataku.scrobscrob.data.repository.ArtworkRepositoryImpl
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.data.repository.ChartRepositoryImpl
import com.mataku.scrobscrob.data.repository.FileRepository
import com.mataku.scrobscrob.data.repository.FileRepositoryImpl
import com.mataku.scrobscrob.data.repository.LicenseRepository
import com.mataku.scrobscrob.data.repository.LicenseRepositoryImpl
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
import com.mataku.scrobscrob.data.repository.UserRepository
import com.mataku.scrobscrob.data.repository.UserRepositoryImpl
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.data.repository.UsernameRepositoryImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface RepositoryModule {

  @Binds
  fun provideSessionRepository(repository: SessionRepositoryImpl): SessionRepository

  @Binds
  fun provideUsernameRepository(repository: UsernameRepositoryImpl): UsernameRepository

  @Binds
  fun provideTopAlbumsRepository(repository: AlbumRepositoryImpl): AlbumRepository

  @Binds
  fun provideTopArtistsRepository(repository: TopArtistsRepositoryImpl): TopArtistsRepository

  @Binds
  fun provideScrobbleRepository(repository: ScrobbleRepositoryImpl): ScrobbleRepository

  @Binds
  fun provideThemeRepository(repository: ThemeRepositoryImpl): ThemeRepository

  @Binds
  fun provideTrackRepository(repository: TrackRepositoryImpl): TrackRepository

  @Binds
  fun provideArtistRepository(repository: ArtistRepositoryImpl): ArtistRepository

  @Binds
  fun provideNowPlayingRepository(repository: NowPlayingRepositoryImpl): NowPlayingRepository

  @Binds
  fun provideScrobbleSettingRepository(repository: ScrobbleSettingRepositoryImpl): ScrobbleSettingRepository

  @Binds
  fun provideChartRepository(repository: ChartRepositoryImpl): ChartRepository

  @Binds
  fun provideLicenseRepository(repository: LicenseRepositoryImpl): LicenseRepository

  @Binds
  fun provideFileRepository(repository: FileRepositoryImpl): FileRepository

  @Binds
  fun provideUserRepository(repository: UserRepositoryImpl): UserRepository

  @Binds
  fun provideArtworkRepository(repository: ArtworkRepositoryImpl): ArtworkRepository
}
