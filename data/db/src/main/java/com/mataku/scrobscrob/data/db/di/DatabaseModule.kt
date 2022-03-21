package com.mataku.scrobscrob.data.db.di

import android.content.Context
import androidx.room.Room
import com.mataku.scrobscrob.data.db.AppDatabase
import com.mataku.scrobscrob.data.db.ScrobbleDao
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideSessionKeyStore(@ApplicationContext context: Context): SessionKeyDataStore {
        return SessionKeyDataStore(context)
    }

    @Singleton
    @Provides
    fun provideUsernameStore(@ApplicationContext context: Context): UsernameDataStore {
        return UsernameDataStore(context)
    }

    @Singleton
    @Provides
    fun provideSunsetDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "sunset_db").build()
    }

    @Singleton
    @Provides
    fun provideScrobbleDao(appDatabase: AppDatabase): ScrobbleDao {
        return appDatabase.scrobbleDao
    }

}