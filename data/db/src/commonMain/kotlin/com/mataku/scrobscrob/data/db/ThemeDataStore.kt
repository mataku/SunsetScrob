package com.mataku.scrobscrob.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@SingleIn(AppScope::class)
class ThemeDataStore(
  private val dataStore: DataStore<Preferences>
) {
  fun theme(): Flow<AppThemeEntity> =
    dataStore.data
      .catch {
        AppThemeEntity.FOLLOW_SYSTEM
      }
      .map {
        AppThemeEntity.find(it[THEME_KEY])
      }

  suspend fun setTheme(theme: AppThemeEntity): Flow<Unit> {
    return flowOf(
      dataStore.edit {
        it[THEME_KEY] = theme.primaryId
      }
    ).flowOn(Dispatchers.IO)
      .map { }
  }

  companion object {
    private val THEME_KEY = intPreferencesKey("current_theme_id")
  }
}
