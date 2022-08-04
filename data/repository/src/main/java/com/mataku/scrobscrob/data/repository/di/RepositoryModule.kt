package com.mataku.scrobscrob.data.repository.di

import android.content.Context
import com.mataku.scrobscrob.data.api.di.ApiModule
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.db.di.DatabaseModule
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepositoryImpl
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [ApiModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {

  @Singleton
  @Provides
  fun provideSessionRepository(repository: SessionRepositoryImpl): SessionRepository {
    return repository
  }

  @Singleton
  @Provides
  fun provideUsernameRepository(@ApplicationContext context: Context): UsernameRepository {
    return UsernameRepositoryImpl(UsernameDataStore(context))
  }

//    @Binds
//    abstract fun provideUsernameRepository(repository: UsernameRepositoryImpl): UsernameRepository

  @Singleton
  @Provides
  fun provideTopAlbumsRepository(repository: TopAlbumsRepositoryImpl): TopAlbumsRepository {
    return repository
  }

  @Singleton
  @Provides
  fun provideTopArtistsRepository(repository: TopArtistsRepositoryImpl): TopArtistsRepository {
    return repository
  }

  @Singleton
  @Provides
  fun provideScrobbleRepository(repository: ScrobbleRepositoryImpl): ScrobbleRepository {
    return repository
  }

  @Singleton
  @Provides
  fun provideThemeRepository(repository: ThemeRepositoryImpl): ThemeRepository {
    return repository
  }

  @Singleton
  @Provides
  fun provideTrackRepository(repository: TrackRepositoryImpl): TrackRepository {
    return repository
  }

}
