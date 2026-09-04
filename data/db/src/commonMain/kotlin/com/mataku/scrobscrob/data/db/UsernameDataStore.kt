package com.mataku.scrobscrob.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
class UsernameDataStore(
  private val dataStore: DataStore<Preferences>
) {
  suspend fun username(): String? {
    val preferences = dataStore.data.first()
    return kotlin.runCatching {
      preferences[USERNAME_KEY]
    }.fold(
      onSuccess = { it },
      onFailure = {
        null
      }
    )
  }

  fun usernameFlow(): Flow<String?> =
    dataStore.data
      .map { preferences ->
        kotlin.runCatching { preferences[USERNAME_KEY] }.getOrNull()
      }
      .flowOn(Dispatchers.IO)

  suspend fun setUsername(username: String): Flow<Unit> {
    return flowOf(
      dataStore.edit {
        it[USERNAME_KEY] = username
      }
    ).flowOn(Dispatchers.IO)
      .map { }
  }

  suspend fun remove() {
    dataStore.edit {
      it.clear()
    }
  }

  private companion object {
    val USERNAME_KEY = stringPreferencesKey("UserName")
  }
}
