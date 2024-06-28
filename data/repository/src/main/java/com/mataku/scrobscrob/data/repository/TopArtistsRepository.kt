package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserTopArtistsEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
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
  private val lastFmService: LastFmService,
  private val artworkDataStore: ArtworkDataStore
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
    val topArtists = response.toTopArtists().map { artist ->
      val imageUrl = artworkDataStore.artwork(
        artist = artist.name
      )
      if (imageUrl != null) {
        artist.imageUrl = imageUrl
      }
      artist
    }
    emit(topArtists)
  }.flowOn(Dispatchers.IO)
}
