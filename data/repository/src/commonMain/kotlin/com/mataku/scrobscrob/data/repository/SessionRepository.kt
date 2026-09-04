package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.api.LastFmApiCredentials
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.request
import com.mataku.scrobscrob.data.api.endpoint.ApiSignature
import com.mataku.scrobscrob.data.api.endpoint.AuthSessionEndpoint
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionBackupPayload
import com.mataku.scrobscrob.data.db.SessionBackupStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn
import java.net.URLEncoder

interface SessionRepository {
  suspend fun authorize(token: String): Flow<Unit>
  fun webAuthUrl(): Flow<String>
  suspend fun logout(): Flow<Unit>
  suspend fun recoverFromKeystoreLossIfNeeded(): Flow<Unit>
  suspend fun restoreSessionFromBackupIfNeeded(): Flow<Unit>
  suspend fun backfillSessionBackup(): Flow<Unit>
}

@SingleIn(AppScope::class)
@Inject
class SessionRepositoryImpl(
  private val lastFmService: LastFmService,
  private val sessionKeyDataStore: SessionKeyDataStore,
  private val usernameDataStore: UsernameDataStore,
  private val scrobbleAppDataStore: ScrobbleAppDataStore,
  private val sessionBackupStore: SessionBackupStore
) :
  SessionRepository {
  override suspend fun authorize(token: String): Flow<Unit> = flow {
    val params = mutableMapOf(
      "token" to token,
      "method" to METHOD
    )
    val apiSig = ApiSignature.generateApiSig(params)
    params["api_sig"] = apiSig
    val endpoint = AuthSessionEndpoint(
      params = params
    )
    val result = lastFmService.request(endpoint).session
    sessionKeyDataStore.setSessionKey(result.key).zip(
      usernameDataStore.setUsername(result.name)
    ) { sessionKeyResult, usernameResult ->
      Pair(sessionKeyResult, usernameResult)
    }.collect {
      sessionBackupStore.save(SessionBackupPayload(sessionKey = result.key, username = result.name))
      emit(Unit)
    }
  }.flowOn(Dispatchers.IO)

  override fun webAuthUrl(): Flow<String> = flowOf(
    "https://www.last.fm/api/auth/?api_key=${LastFmApiCredentials.API_KEY}&cb=${URLEncoder.encode(CALLBACK_URL, Charsets.UTF_8.name())}"
  )

  override suspend fun logout(): Flow<Unit> = flow {
    sessionKeyDataStore.remove()
    usernameDataStore.remove()
    scrobbleAppDataStore.clear()
    sessionBackupStore.clear()
    emit(Unit)
  }

  override suspend fun recoverFromKeystoreLossIfNeeded(): Flow<Unit> = flow {
    val sessionKey = sessionKeyDataStore.sessionKey()
    val username = usernameDataStore.username()
    if (sessionKey == null && username != null) {
      sessionKeyDataStore.remove()
      usernameDataStore.remove()
      scrobbleAppDataStore.clear()
    }
    emit(Unit)
  }.flowOn(Dispatchers.IO)

  override suspend fun restoreSessionFromBackupIfNeeded(): Flow<Unit> = flow {
    val localSessionKey = sessionKeyDataStore.sessionKey()
    if (localSessionKey == null) {
      val payload = sessionBackupStore.restore()
      if (payload != null) {
        sessionKeyDataStore.setSessionKey(payload.sessionKey).collect()
        usernameDataStore.setUsername(payload.username).collect()
      }
    }
    emit(Unit)
  }.flowOn(Dispatchers.IO)

  override suspend fun backfillSessionBackup(): Flow<Unit> = flow {
    val localSessionKey = sessionKeyDataStore.sessionKey()
    if (localSessionKey != null) {
      val localUsername = usernameDataStore.username()
      if (localUsername != null) {
        sessionBackupStore.save(SessionBackupPayload(sessionKey = localSessionKey, username = localUsername))
      }
    }
    emit(Unit)
  }.flowOn(Dispatchers.IO)

  companion object {
    private const val METHOD = "auth.getSession"
    private const val CALLBACK_URL = "https://sunsetscrob.mataku.com/auth/lastfm"
  }
}
