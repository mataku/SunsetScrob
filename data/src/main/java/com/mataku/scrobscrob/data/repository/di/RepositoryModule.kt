package com.mataku.scrobscrob.data.repository.di

import com.mataku.scrobscrob.data.api.di.ApiModule
import com.mataku.scrobscrob.data.db.di.DatabaseModule
import com.mataku.scrobscrob.data.repository.MobileSessionRepository
import com.mataku.scrobscrob.data.repository.MobileSessionRepositoryImpl
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.NowPlayingRepositoryImpl
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.data.repository.TrackRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [ApiModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    internal fun provideTrackRepository(trackRepositoryImpl: TrackRepositoryImpl): TrackRepository {
        return trackRepositoryImpl
    }

    @Provides
    @Singleton
    internal fun provideNowPlayingRepository(nowPlayingRepositoryImpl: NowPlayingRepositoryImpl): NowPlayingRepository {
        return nowPlayingRepositoryImpl
    }

    @Provides
    @Singleton
    internal fun provideMobileSessionRepository(mobileSessionRepositoryImpl: MobileSessionRepositoryImpl): MobileSessionRepository {
        return mobileSessionRepositoryImpl
    }
}