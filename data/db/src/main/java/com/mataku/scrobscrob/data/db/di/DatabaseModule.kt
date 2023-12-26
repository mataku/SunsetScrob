package com.mataku.scrobscrob.data.db.di

import android.content.Context
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.ThemeDataStore
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
  fun provideThemeDataStore(@ApplicationContext context: Context): ThemeDataStore {
    return ThemeDataStore(context)
  }

  @Singleton
  @Provides
  fun provideScrobbleAppDataStore(@ApplicationContext context: Context): ScrobbleAppDataStore {
    return ScrobbleAppDataStore(context)
  }
}
