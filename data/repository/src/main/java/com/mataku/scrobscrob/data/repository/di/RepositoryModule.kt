package com.mataku.scrobscrob.data.repository.di

import android.content.Context
import com.mataku.scrobscrob.data.api.di.ApiModule
import com.mataku.scrobscrob.data.db.di.DatabaseModule
import com.mataku.scrobscrob.data.repository.MobileSessionRepository
import com.mataku.scrobscrob.data.repository.MobileSessionRepositoryImpl
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepositoryImpl
import com.mataku.scrobscrob.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.data.repository.TopAlbumsRepositoryImpl
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.TopArtistsRepositoryImpl
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
    fun provideMobileSessionRepository(mobileSessionRepository: MobileSessionRepositoryImpl): MobileSessionRepository {
        return mobileSessionRepository
    }

    @Singleton
    @Provides
    fun provideUsernameRepository(@ApplicationContext context: Context): UsernameRepository {
        val sharedPref = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        return UsernameRepositoryImpl(sharedPref)
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
}