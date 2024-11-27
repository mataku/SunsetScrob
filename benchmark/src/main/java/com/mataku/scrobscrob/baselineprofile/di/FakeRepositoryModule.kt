package com.mataku.scrobscrob.baselineprofile.di

import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FakeRepositoryModule {
  @Provides
  fun provideUsernameRepository(): UsernameRepository {
    return FakeUsernameRepository()
  }

  @Provides
  fun provideScrobbleRepository(): ScrobbleRepository {
    return FakeScrobbleRepository()
  }

  @Provides
  fun provideTopArtistsRepository(): TopArtistsRepository {
    return FakeTopArtistsRepository()
  }
}
