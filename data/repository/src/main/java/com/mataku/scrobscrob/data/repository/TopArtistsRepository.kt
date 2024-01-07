package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserTopArtistsEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toTopArtists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface TopArtistsRepository {
  suspend fun fetchTopArtists(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopArtistInfo>>
}

@Singleton
class TopArtistsRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : TopArtistsRepository {
  override suspend fun fetchTopArtists(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopArtistInfo>> = flow {
    val params = mapOf(
      "limit" to 20,
      "page" to page,
      "period" to timeRangeFiltering.rawValue,
      "user" to username,
    )
    val endpoint = UserTopArtistsEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toTopArtists())
  }.flowOn(Dispatchers.IO)
}
