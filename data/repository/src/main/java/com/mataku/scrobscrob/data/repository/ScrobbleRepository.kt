package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ApiSignature
import com.mataku.scrobscrob.data.api.endpoint.ScrobbleEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserRecentTracksEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.repository.mapper.toRecentTracks
import com.mataku.scrobscrob.data.repository.mapper.toScrobbleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ScrobbleRepository {
  suspend fun recentTracks(page: Int): Flow<List<RecentTrack>>

  suspend fun scrobble(currentTrack: NowPlayingTrackEntity): Flow<ScrobbleResult>
}

@Singleton
class ScrobbleRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore,
  private val sessionDataStore: SessionKeyDataStore,
  private val artworkDataStore: ArtworkDataStore
) : ScrobbleRepository {
  override suspend fun recentTracks(page: Int): Flow<List<RecentTrack>> = flow {
    val username = usernameDataStore.username() ?: emit(emptyList())

    val params = mapOf(
      "user" to username,
      "limit" to 50,
      "page" to page
    )

    val endpoint = UserRecentTracksEndpoint(
      params = params
    )

    val response = lastFmService.request(endpoint)
    val recentTracks = response.toRecentTracks()
    emit(recentTracks)
    recentTracks.distinct().forEach { track ->
      val imageUrl = track.images.imageUrl()
      if (imageUrl != null) {
        artworkDataStore.insertArtwork(
          albumName = track.albumName,
          artist = track.artistName,
          artworkUrl = imageUrl
        )
      }
    }
  }

  override suspend fun scrobble(currentTrack: NowPlayingTrackEntity) = flow {
    val sessionKey = sessionDataStore.sessionKey()
    if (sessionKey.isNullOrEmpty()) {
      emit(ScrobbleResult(accepted = false))
      return@flow
    }
    if (currentTrack.overScrobblePoint()) {
      val params = mutableMapOf(
        "album[0]" to currentTrack.albumName,
        "artist[0]" to currentTrack.artistName,
        "sk" to sessionKey,
        "timestamp[0]" to currentTrack.timeStamp.toString(),
        "track[0]" to currentTrack.trackName,
        "method" to "track.scrobble"
      )
      val apiSig = ApiSignature.generateApiSig(params)
      params.remove("method")
      params["api_sig"] = apiSig
      val endpoint = ScrobbleEndpoint(
        params = params
      )
      val response = lastFmService.request(endpoint)
      emit(response.toScrobbleResult())
    } else {
      emit(ScrobbleResult(false))
    }

  }.flowOn(Dispatchers.IO)
}
