package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.NowPlayingTrack
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UpdateNowPlayingEndpoint
import com.mataku.scrobscrob.data.db.NowPlayingDao
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.repository.mapper.toNowPlayingTrack
import com.mataku.scrobscrob.data.repository.mapper.toNowPlayingTrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NowPlayingRepository {
  suspend fun update(trackInfo: TrackInfo): Flow<Unit>

  suspend fun current(): NowPlayingTrack?
}

class NowPlayingRepositoryImpl(
  private val lastFmService: LastFmService,
  private val sessionKeyDataStore: SessionKeyDataStore,
  private val nowPlayingDao: NowPlayingDao
) : NowPlayingRepository {

  override suspend fun current(): NowPlayingTrack? =
    nowPlayingDao.nowPlaying()?.toNowPlayingTrack()

  override suspend fun update(trackInfo: TrackInfo) = flow {
    val entity = trackInfo.toNowPlayingTrackEntity()
    nowPlayingDao.insert(entity)
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
