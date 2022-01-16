package com.mataku.scrobscrob.data.repository.di

import com.mataku.scrobscrob.data.api.di.ApiModule
import com.mataku.scrobscrob.data.db.di.DatabaseModule
import com.mataku.scrobscrob.data.repository.MobileSessionRepository
import com.mataku.scrobscrob.data.repository.MobileSessionRepositoryImpl
import com.mataku.scrobscrob.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.data.repository.TopAlbumsRepositoryImpl
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.data.repository.UsernameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [ApiModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMobileSessionRepository(mobileSessionRepository: MobileSessionRepositoryImpl): MobileSessionRepository

    @Binds
    abstract fun provideUsernameRepository(repository: UsernameRepositoryImpl): UsernameRepository

    @Binds
    abstract fun provideTopAlbumsRepository(repository: TopAlbumsRepositoryImpl): TopAlbumsRepository
}