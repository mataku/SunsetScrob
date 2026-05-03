package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.RecentTracks
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.request
import com.mataku.scrobscrob.data.api.endpoint.ApiSignature
import com.mataku.scrobscrob.data.api.endpoint.ScrobbleEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserRecentTracksEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.db.entity.ArtworkInsertion
import com.mataku.scrobscrob.data.repository.mapper.toRecentTracks
import com.mataku.scrobscrob.data.repository.mapper.toScrobbleResult
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface ScrobbleRepository {
  suspend fun recentTracks(page: Int): Flow<RecentTracks>

  suspend fun scrobble(currentTrack: NowPlayingTrackEntity): Flow<ScrobbleResult>
}

@SingleIn(AppScope::class)
@Inject
class ScrobbleRepositoryImpl(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore,
  private val sessionDataStore: SessionKeyDataStore,
  private val artworkDataStore: ArtworkDataStore
) : ScrobbleRepository {
  override suspend fun recentTracks(page: Int): Flow<RecentTracks> = flow {
    val username = usernameDataStore.username()
    if (username == null) {
      emit(RecentTracks(tracks = persistentListOf(), pagingAttr = PagingAttr()))
      return@flow
    }

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
    val insertions = recentTracks.tracks.distinct().mapNotNull { track ->
      val imageUrl = track.images.imageUrl() ?: return@mapNotNull null
      ArtworkInsertion(
        albumName = track.albumName,
        artist = track.artistName,
        artworkUrl = imageUrl,
      )
    }
    artworkDataStore.insertArtworks(insertions)
  }.flowOn(Dispatchers.IO)

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
