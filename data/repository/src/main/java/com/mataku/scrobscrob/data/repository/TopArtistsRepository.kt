package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtists
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserTopArtistsEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.repository.mapper.toTopArtists
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface TopArtistsRepository {
  suspend fun fetchTopArtists(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<TopArtists>
}

@SingleIn(AppScope::class)
@Inject
class TopArtistsRepositoryImpl(
  private val lastFmService: LastFmService,
  private val artworkDataStore: ArtworkDataStore
) : TopArtistsRepository {
  override suspend fun fetchTopArtists(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<TopArtists> = flow {
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
    val result = response.toTopArtists()
    val artistsWithArtwork = result.artists.map { artist ->
      val imageUrl = artworkDataStore.artwork(
        artist = artist.name
      )
      if (imageUrl != null) {
        artist.copy(imageUrl = imageUrl)
      } else {
        artist
      }
    }.toImmutableList()
    emit(result.copy(artists = artistsWithArtwork))
  }.flowOn(Dispatchers.IO)
}
