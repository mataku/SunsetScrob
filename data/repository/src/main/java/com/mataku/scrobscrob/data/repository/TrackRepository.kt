package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoEndpoint
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.repository.mapper.toTrackInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface TrackRepository {
  suspend fun getInfo(trackName: String, artistName: String): Flow<TrackInfo>
}

class TrackRepositoryImpl(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore
) :
  TrackRepository {
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
}
