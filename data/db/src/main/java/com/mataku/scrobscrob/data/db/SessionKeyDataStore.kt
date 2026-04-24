package com.mataku.scrobscrob.data.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

private val Context.sessionKeyDataStore by preferencesDataStore("SESSION_KEY")

@SingleIn(AppScope::class)
class SessionKeyDataStore(
  private val context: Context
) {
  suspend fun sessionKey(): String? {
    val preferences = context.sessionKeyDataStore.data.first()
    return kotlin.runCatching {
      preferences[SESSION_KEY]
    }.fold(
      onSuccess = {
        it
      },
      onFailure = {
        null
      }
    )
  }

  suspend fun setSessionKey(sessionKey: String): Flow<Unit> {
    return flowOf(
      context.sessionKeyDataStore.edit {
        it[SESSION_KEY] = sessionKey
      }
    ).map { }
  }

  suspend fun remove() {
    context.sessionKeyDataStore.edit {
      it.clear()
    }
  }

  companion object {
    private val SESSION_KEY = stringPreferencesKey("session_key")
  }
}
