package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ApiSignature
import com.mataku.scrobscrob.data.api.endpoint.LoveTrackEndpoint
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UnLoveTrackEndpoint
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.repository.mapper.toTrackInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface TrackRepository {
  suspend fun getInfo(trackName: String, artistName: String): Flow<TrackInfo>

  suspend fun loveTrack(trackName: String, artistName: String): Flow<Unit>
  suspend fun unloveTrack(trackName: String, artistName: String): Flow<Unit>
}

@Singleton
class TrackRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore,
  private val sessionKeyDataStore: SessionKeyDataStore,
) : TrackRepository {
  override suspend fun getInfo(
    trackName: String,
    artistName: String
  ): Flow<TrackInfo> = flow {
    val username = usernameDataStore.username()
    val params = mapOf(
      "artist" to artistName,
      "track" to trackName,
      "username" to username
    )
    val endpoint = TrackInfoEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toTrackInfo())
  }.flowOn(Dispatchers.IO)

  override suspend fun loveTrack(trackName: String, artistName: String): Flow<Unit> = flow {
    val sessionKey = sessionKeyDataStore.sessionKey()
      ?: throw IllegalStateException("Session key is not found")

    val params = mutableMapOf(
      "artist" to artistName,
      "track" to trackName,
      "method" to LoveTrackEndpoint.METHOD,
      "sk" to sessionKey
    )
    val apiSig = ApiSignature.generateApiSig(params)
    params.remove("method")
    params["api_sig"] = apiSig
    val endpoint = LoveTrackEndpoint(
      params = params
    )
    lastFmService.request(endpoint)
    emit(Unit)
  }.flowOn(Dispatchers.IO)

  override suspend fun unloveTrack(trackName: String, artistName: String): Flow<Unit> = flow {
    val sessionKey = sessionKeyDataStore.sessionKey()
      ?: throw IllegalStateException("Session key is not found")

    val params = mutableMapOf(
      "artist" to artistName,
      "track" to trackName,
      "method" to UnLoveTrackEndpoint.METHOD,
      "sk" to sessionKey
    )
    val apiSig = ApiSignature.generateApiSig(params)
    params.remove("method")
    params["api_sig"] = apiSig
    val endpoint = UnLoveTrackEndpoint(
      params = params
    )
    lastFmService.request(endpoint)
    emit(Unit)
  }.flowOn(Dispatchers.IO)
}

