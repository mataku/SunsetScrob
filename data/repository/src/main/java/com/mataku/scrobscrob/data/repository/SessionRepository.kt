package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.AuthMobileSessionEndpoint
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import javax.inject.Inject
import javax.inject.Singleton

interface SessionRepository {
  suspend fun authorize(userName: String, password: String): Flow<Unit>
  suspend fun logout(): Flow<Unit>
}

@Singleton
class SessionRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val sessionKeyDataStore: SessionKeyDataStore,
  private val usernameDataStore: UsernameDataStore,
  private val scrobbleAppDataStore: ScrobbleAppDataStore
) :
  SessionRepository {
  override suspend fun authorize(userName: String, password: String): Flow<Unit> = flow {
    val params = mutableMapOf(
      "username" to userName,
      "password" to password,
      "method" to METHOD
    )
    val apiSig = ApiSignature.generateApiSig(params)
    params["api_sig"] = apiSig
    val endpoint = AuthMobileSessionEndpoint(
      params = params
    )
    val result = lastFmService.request(endpoint).mobileSession
    sessionKeyDataStore.setSessionKey(result.key).zip(
      usernameDataStore.setUsername(result.name)
    ) { sessionKeyResult, usernameResult ->
      Pair(sessionKeyResult, usernameResult)
    }.collect {
      emit(Unit)
    }
  }.flowOn(Dispatchers.IO)

  override suspend fun logout(): Flow<Unit> = flow {
    sessionKeyDataStore.remove()
    usernameDataStore.remove()
    scrobbleAppDataStore.clear()
    emit(Unit)
  }

  companion object {
    private const val METHOD = "auth.getMobileSession"
  }
}
