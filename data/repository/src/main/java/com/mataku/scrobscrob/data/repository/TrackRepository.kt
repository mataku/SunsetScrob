package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.TrackInfo
import com.mataku.scrobscrob.core.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TrackInfoEndpoint
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface TrackRepository {
  suspend fun getInfo(trackName: String, artistName: String): Flow<TrackInfo>
}

@Singleton
class TrackRepositoryImpl @Inject constructor(
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
    emit(
      lastFmService.get<TrackInfoApiResponse>(TrackInfoEndpoint(params = params)).trackInfo
    )
  }
}
