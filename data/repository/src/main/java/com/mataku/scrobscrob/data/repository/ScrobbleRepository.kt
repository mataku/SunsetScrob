package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserRecentTracksEndpoint
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.repository.mapper.toRecentTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface ScrobbleRepository {
  suspend fun recentTracks(page: Int): Flow<List<RecentTrack>>
}

@Singleton
class ScrobbleRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore
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
}
