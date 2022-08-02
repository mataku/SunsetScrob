package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.core.api.endpoint.RecentTrackApiResponse
import com.mataku.scrobscrob.core.api.endpoint.RecentTracksEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.UsernameDataStore
import javax.inject.Inject
import javax.inject.Singleton

interface ScrobbleRepository {
  suspend fun recentTracks(page: Int): SunsetResult<List<RecentTrack>>
}

@Singleton
class ScrobbleRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore
) : ScrobbleRepository {
  override suspend fun recentTracks(page: Int): SunsetResult<List<RecentTrack>> {
    val username = usernameDataStore.username() ?: return SunsetResult.success(emptyList())

    val params = mapOf(
      "user" to username,
      "limit" to 50,
      "page" to page
    )

    return try {
      val result = lastFmService.get<RecentTrackApiResponse>(
        RecentTracksEndpoint(params = params)
      )
      SunsetResult.success(result.recentTracks.tracks)
    } catch (e: Throwable) {
      SunsetResult.failure(e)
    }
  }
}
