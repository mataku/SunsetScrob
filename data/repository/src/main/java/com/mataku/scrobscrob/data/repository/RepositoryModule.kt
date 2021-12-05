package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.api.di.ApiModule
import com.mataku.scrobscrob.data.db.di.DatabaseModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [ApiModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMobileSessionRepository(mobileSessionRepository: MobileSessionRepositoryImpl): MobileSessionRepository
}