package com.mataku.scrobscrob.data.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mataku.scrobscrob.core.entity.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

private val Context.themeDataStore by preferencesDataStore("DATA")

@Singleton
class ThemeDataStore(
    @ApplicationContext private val context: Context
) {
    suspend fun theme(): Flow<AppTheme> {
        return flow {
            val preferences = context.themeDataStore.data.first()
            val rawValue = preferences[THEME_KEY]
            emit(AppTheme.deserialize(rawValue))
        }
    }

    suspend fun setTheme(theme: AppTheme): Flow<Unit> {
        return flowOf(
            context.themeDataStore.edit {
                it[THEME_KEY] = theme.rawValue
            }
        ).flowOn(Dispatchers.IO)
            .map { }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("current_theme")
    }
}