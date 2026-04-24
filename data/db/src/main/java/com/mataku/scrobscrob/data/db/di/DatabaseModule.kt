package com.mataku.scrobscrob.data.db.di

import android.content.Context
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.ArtworkDataStoreImpl
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.ThemeDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface DatabaseModule {

  @SingleIn(AppScope::class)
  @Provides
  fun provideSessionKeyStore(context: Context): SessionKeyDataStore {
    return SessionKeyDataStore(context)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideUsernameStore(context: Context): UsernameDataStore {
    return UsernameDataStore(context)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideThemeDataStore(context: Context): ThemeDataStore {
    return ThemeDataStore(context)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideScrobbleAppDataStore(context: Context): ScrobbleAppDataStore {
    return ScrobbleAppDataStore(context)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideArtworkDataStore(context: Context): ArtworkDataStore {
    return ArtworkDataStoreImpl(context)
  }
}
