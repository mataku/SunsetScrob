package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ScrobbleEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserRecentTracksEndpoint
import com.mataku.scrobscrob.data.db.NowPlayingDao
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
}

@Singleton
class ScrobbleRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore,
  private val sessionDataStore: SessionKeyDataStore,
  private val nowPlayingDao: NowPlayingDao
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
    emit(response.toRecentTracks())
  }

  suspend fun scrobble() = flow {
    val currentTrack = nowPlayingDao.nowPlaying()
    val sessionKey = sessionDataStore.sessionKey()
    if (currentTrack == null || sessionKey == null) {
      emit(ScrobbleResult(accepted = false))
      return@flow
    }
    val params = mapOf(
      "album[0]" to currentTrack.albumName,
      "artist[0]" to currentTrack.artistName,
      "sk" to sessionKey,
      "timestamp[0]" to currentTrack.timeStamp,
      "track[0]" to currentTrack.trackName
    )
    val endpoint = ScrobbleEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toScrobbleResult())
  }.flowOn(Dispatchers.IO)
}
