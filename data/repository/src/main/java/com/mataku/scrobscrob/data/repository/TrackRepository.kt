package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.TrackInfo
import com.mataku.scrobscrob.core.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TrackInfoEndpoint
import com.mataku.scrobscrob.data.api.LastFmService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

interface TrackRepository {
  suspend fun getInfo(trackName: String, artistName: String): Flow<TrackInfo>
}

@Singleton
class TrackRepositoryImpl @Inject constructor(private val lastFmService: LastFmService) :
  TrackRepository {
  override suspend fun getInfo(trackName: String, artistName: String): Flow<TrackInfo> {
    val params = mapOf(
      "artist" to artistName,
      "track" to trackName
    )
    return flowOf(
      lastFmService.get<TrackInfoApiResponse>(TrackInfoEndpoint(params = params)).trackInfo
    )
  }
}
