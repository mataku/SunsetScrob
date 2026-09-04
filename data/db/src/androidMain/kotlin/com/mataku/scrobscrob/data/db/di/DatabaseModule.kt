package com.mataku.scrobscrob.data.db.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mataku.scrobscrob.Database
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.ArtworkDataStoreImpl
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.ThemeDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.db.encryptedSessionKeyDataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

private val Context.usernameDataStore: DataStore<Preferences> by preferencesDataStore("USERNAME")
private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore("THEME")
private val Context.scrobbleAppDataStore: DataStore<Preferences> by preferencesDataStore("ScrobbleApp")

@ContributesTo(AppScope::class)
interface DatabaseModule {

  @SingleIn(AppScope::class)
  @Provides
  fun provideSessionKeyStore(context: Context): SessionKeyDataStore {
    return SessionKeyDataStore(encryptedSessionKeyDataStore(context))
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideUsernameStore(context: Context): UsernameDataStore {
    return UsernameDataStore(context.usernameDataStore)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideThemeDataStore(context: Context): ThemeDataStore {
    return ThemeDataStore(context.themeDataStore)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideScrobbleAppDataStore(context: Context): ScrobbleAppDataStore {
    return ScrobbleAppDataStore(context.scrobbleAppDataStore)
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideArtworkDataStore(context: Context): ArtworkDataStore {
    return ArtworkDataStoreImpl(AndroidSqliteDriver(Database.Schema, context, "scrobscrob.db"))
  }
}
