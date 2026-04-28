package com.mataku.scrobscrob.data.db

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

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
        if (rawPrimaryId == null) {
          systemDefaultTheme()
        } else {
          AppThemeEntity.find(rawPrimaryId)
        }
      }

  suspend fun setTheme(theme: AppThemeEntity): Flow<Unit> {
    return flowOf(
      context.themeDataStore.edit {
        it[THEME_KEY] = theme.primaryId
      }
    ).flowOn(Dispatchers.IO)
      .map { }
  }

  private fun systemDefaultTheme(): AppThemeEntity {
    val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
      AppThemeEntity.DARK
    } else {
      AppThemeEntity.LIGHT
    }
  }

  companion object {
    private val THEME_KEY = intPreferencesKey("current_theme_id")
  }
}
