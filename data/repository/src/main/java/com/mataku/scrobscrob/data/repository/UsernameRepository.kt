package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.runBlocking

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

  suspend fun asyncUsername(): String?
}

class UsernameRepositoryImpl(
  private val usernameDataStore: UsernameDataStore
) : UsernameRepository {
  override fun username(): String? =
    runBlocking { usernameDataStore.username() }

  override suspend fun asyncUsername(): String? =
    usernameDataStore.username()
}
