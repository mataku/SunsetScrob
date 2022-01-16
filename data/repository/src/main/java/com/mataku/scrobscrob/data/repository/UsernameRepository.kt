package com.mataku.scrobscrob.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

//interface UsernameRepository {
//    suspend fun username(): Flow<String?>
//    suspend fun setUsername(username: String): Flow<Unit>
//}
//
//@Singleton
//class UsernameRepositoryImpl @Inject constructor(
//    private val dataStore: UsernameDataStore
//) : UsernameRepository {
//    override suspend fun username(): Flow<String?> = flow {
//        emit(dataStore.username())
//    }.flowOn(Dispatchers.IO)
//
//    override suspend fun setUsername(username: String): Flow<Unit> {
//        return dataStore.setUsername(username = username)
//    }
//}

interface UsernameRepository {
    fun username(): String?
    fun setUsername(username: String)
}

@Singleton
class UsernameRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferences
) : UsernameRepository {
    override fun username(): String? =
        sharedPref.getString(USERNAME_KEY, null)

    override fun setUsername(username: String) {
        sharedPref.edit().putString(USERNAME_KEY, username).apply()
    }

    private companion object {
        const val USERNAME_KEY = "UserName"
    }
}