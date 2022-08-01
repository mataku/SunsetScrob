package com.mataku.scrobscrob.data.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

private val Context.sessionKeyDataStore by preferencesDataStore("SESSION_KEY")

@Singleton
class SessionKeyDataStore(
    @ApplicationContext private val context: Context
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

    suspend fun remove(): Flow<Unit> {
        return flowOf(
            context.sessionKeyDataStore.edit {
                it.clear()
            }
        ).flowOn(Dispatchers.IO)
            .map { }
    }

    companion object {
        private val SESSION_KEY = stringPreferencesKey("session_key")
    }
}