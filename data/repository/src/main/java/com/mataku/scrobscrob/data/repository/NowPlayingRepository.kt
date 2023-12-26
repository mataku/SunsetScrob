package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ApiSignature
import com.mataku.scrobscrob.data.api.endpoint.UpdateNowPlayingEndpoint
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.repository.mapper.toNowPlayingTrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface NowPlayingRepository {
  suspend fun update(trackInfo: TrackInfo): Flow<Unit>

  suspend fun current(): NowPlayingTrackEntity?
}

@Singleton
class NowPlayingRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val sessionKeyDataStore: SessionKeyDataStore,
) : NowPlayingRepository {

  private var currentNowPlayingTrackInfo: NowPlayingTrackEntity? = null

  override suspend fun current(): NowPlayingTrackEntity? = currentNowPlayingTrackInfo

  override suspend fun update(trackInfo: TrackInfo) = flow {
    currentNowPlayingTrackInfo = trackInfo.toNowPlayingTrackEntity()
    updateNowPlaying(
      trackName = trackInfo.name,
      artistName = trackInfo.artist.name,
      albumName = trackInfo.album?.title ?: ""
    )
    emit(Unit)
  }

  private suspend fun updateNowPlaying(
    trackName: String,
    artistName: String,
    albumName: String
  ) {
    val sessionKey = sessionKeyDataStore.sessionKey()
    if (sessionKey == null) {
      return
    } else {
      val params = mutableMapOf(
        "artist" to artistName,
        "track" to trackName,
        "album" to albumName,
        "method" to NOW_PLAYING_METHOD,
        "sk" to sessionKey
      )
      val apiSig = ApiSignature.generateApiSig(params)
      params.remove("method")
      params["api_sig"] = apiSig
      val endpoint = UpdateNowPlayingEndpoint(
        params = params
      )
      lastFmService.request(endpoint)
    }
  }

  companion object {
    private const val NOW_PLAYING_METHOD = "track.updateNowPlaying"
  }
}
