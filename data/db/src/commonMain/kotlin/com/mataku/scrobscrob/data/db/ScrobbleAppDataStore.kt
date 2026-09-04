package com.mataku.scrobscrob.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
class ScrobbleAppDataStore(
  private val dataStore: DataStore<Preferences>
) {
  suspend fun allowedApps(): Set<String> {
    val preferences = dataStore.data.first()
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

  suspend fun allowedAppsFlow(): Flow<Set<String>> {
    return dataStore.data.map {
      it[ALLOWED_PACKAGES_KEY] ?: emptySet()
    }
  }

  suspend fun allowApp(appName: String): Flow<Unit> = flow {
    val packages = allowedApps().toMutableSet()
    packages.add(appName)
    dataStore.edit {
      it[ALLOWED_PACKAGES_KEY] = packages
    }

    emit(Unit)
  }.flowOn(Dispatchers.IO)

  suspend fun disallowApp(appName: String): Flow<Unit> = flow {
    val packages = allowedApps().toMutableSet()
    packages.remove(appName)
    dataStore.edit {
      it[ALLOWED_PACKAGES_KEY] = packages
    }
    emit(Unit)
  }.flowOn(Dispatchers.IO)

  suspend fun clear() {
    dataStore.edit {
      it.clear()
    }
  }

  private companion object {
    val ALLOWED_PACKAGES_KEY = stringSetPreferencesKey("allowed_packages_key")
  }
}
