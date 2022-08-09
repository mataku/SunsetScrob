package com.mataku.scrobscrob.data.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("ScrobbleApp")

@Singleton
class ScrobbleAppDataStore(
  @ApplicationContext private val context: Context
) {
  suspend fun allowedApps(): Set<String> {
    val preferences = context.dataStore.data.first()
    return kotlin.runCatching {
      preferences[ALLOWED_PACKAGES_KEY]
    }.fold(
      onSuccess = {
        it ?: emptySet()
      },
      onFailure = {
        emptySet()
      }
    )
  }

  suspend fun allowApp(appName: String): Flow<Unit> = flow {
    val packages = allowedApps().toMutableSet()
    packages.add(appName)
    context.dataStore.edit {
      it[ALLOWED_PACKAGES_KEY] = packages
    }
    emit(Unit)
  }.flowOn(Dispatchers.IO)

  private companion object {
    val ALLOWED_PACKAGES_KEY = stringSetPreferencesKey("allowed_packages_key")
  }
}
