package com.mataku.scrobscrob.data.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

private val Context.themeDataStore by preferencesDataStore("THEME")

@SingleIn(AppScope::class)
class ThemeDataStore(
  private val context: Context
) {
  fun theme(): Flow<AppThemeEntity> =
    context.themeDataStore.data
      .catch {
        AppThemeEntity.DARK
      }
      .map {
        val rawPrimaryId = it[THEME_KEY]
        AppThemeEntity.find(rawPrimaryId)
      }

  suspend fun setTheme(theme: AppThemeEntity): Flow<Unit> {
    return flowOf(
      context.themeDataStore.edit {
        it[THEME_KEY] = theme.primaryId
      }
    ).flowOn(Dispatchers.IO)
      .map { }
  }

  companion object {
    private val THEME_KEY = intPreferencesKey("current_theme_id")
  }
}
