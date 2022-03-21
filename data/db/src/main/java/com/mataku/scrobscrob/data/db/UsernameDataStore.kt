package com.mataku.scrobscrob.data.db

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

//private val Context.dataStore by preferencesDataStore("USERNAME")

@Singleton
class UsernameDataStore(
    @ApplicationContext private val context: Context
) {
//    suspend fun username(): String? {
//        val preferences = context.dataStore.data.first()
//        return kotlin.runCatching {
//            preferences[USERNAME_KEY]
//        }.fold(
//            onSuccess = { it },
//            onFailure = {
//                null
//            }
//        )
//    }
//
//    suspend fun setUsername(username: String): Flow<Unit> {
//        return flowOf(
//            context.dataStore.edit {
//                it[USERNAME_KEY] = username
//            }
//        ).flowOn(Dispatchers.IO)
//            .map { }
//    }

    private val sharedPref = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)

    fun username(): String? {
        return sharedPref.getString(USERNAME_KEY, null)
    }

    fun setUsername(username: String) {
        sharedPref.edit().putString(USERNAME_KEY, username).apply()
    }

    fun remove() {
        sharedPref.edit().clear().apply()
    }

//    private companion object {
//        val USERNAME_KEY = stringPreferencesKey("UserName")
//    }

    private companion object {
        const val USERNAME_KEY = "UserName"
    }
}