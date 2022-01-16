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

private val Context.dataStore by preferencesDataStore("DATA")

@Singleton
class UsernameDataStore(
    @ApplicationContext private val context: Context
) {
    suspend fun username(): String? {
        val preferences = context.dataStore.data.first()
        return kotlin.runCatching {
            preferences[USERNAME_KEY]
        }.fold(
            onSuccess = { it },
            onFailure = {
                null
            }
        )
    }

    suspend fun setUsername(username: String): Flow<Unit> {
        return flowOf(
            context.dataStore.edit {
                it[USERNAME_KEY] = username
            }
        ).flowOn(Dispatchers.IO)
            .map { }
    }

    private companion object {
        val USERNAME_KEY = stringPreferencesKey("UserName")
    }
}