package com.mataku.scrobscrob.data.db.di

import android.content.Context
import androidx.room.Room
import com.mataku.scrobscrob.data.db.AppDatabase
import com.mataku.scrobscrob.data.db.NowPlayingDao
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.ScrobbleDao
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.ThemeDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module
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
  fun provideThemeDataStore(@ApplicationContext context: Context): ThemeDataStore {
    return ThemeDataStore(context)
  }

  @Singleton
  @Provides
  fun provideScrobbleAppDataStore(@ApplicationContext context: Context): ScrobbleAppDataStore {
    return ScrobbleAppDataStore(context)
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

  @Singleton
  @Provides
  fun provideNowPlayingDao(appDatabase: AppDatabase): NowPlayingDao {
    return appDatabase.nowPlayingDao
  }
}

val databaseModule = module {
  single {
    SessionKeyDataStore(get())
  }

  single {
    UsernameDataStore(get())
  }

  single {
    ThemeDataStore(get())
  }

  single {
    ScrobbleAppDataStore(get())
  }

  single {
    Room.databaseBuilder(get(), AppDatabase::class.java, "sunset_db").build()
  }
}
