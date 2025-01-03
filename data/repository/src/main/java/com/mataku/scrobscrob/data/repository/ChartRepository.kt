package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.isInvalidArtwork
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ChartTopArtistsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ChartTopTagsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ChartTopTracksEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.repository.mapper.toChartTopArtists
import com.mataku.scrobscrob.data.repository.mapper.toChartTopTags
import com.mataku.scrobscrob.data.repository.mapper.toChartTopTracks
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ChartRepository {
  fun topArtists(page: Int): Flow<ChartTopArtists>
  suspend fun topArtistsAsync(page: Int): Result<ChartTopArtists>

  fun topTracks(page: Int): Flow<ChartTopTracks>
  suspend fun topTracksAsync(page: Int): Result<ChartTopTracks>

  fun topTags(page: Int): Flow<ImmutableList<Tag>>
  suspend fun topTagsAsync(page: Int): Result<ImmutableList<Tag>>
}

@Singleton
class ChartRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val artworkDataStore: ArtworkDataStore
) : ChartRepository {
  override fun topArtists(page: Int): Flow<ChartTopArtists> = flow {
    val params = mapOf(
      "limit" to "10",
      "page" to page.toString()
    )
    val chartTopArtistsEndpoint = ChartTopArtistsEndpoint(
      params = params
    )
    val response = lastFmService.request(chartTopArtistsEndpoint)
    val topArtists = response.toChartTopArtists().topArtists.map { artist ->
      val imageUrl = artworkDataStore.artwork(
        artist = artist.name
      )
      if (imageUrl != null) {
        artist.imageUrl = imageUrl
      }
      artist
    }

    emit(
      ChartTopArtists(
        topArtists = topArtists,
        pagingAttr = response.toChartTopArtists().pagingAttr
      )
    )
  }.flowOn(Dispatchers.IO)

  override fun topTracks(page: Int): Flow<ChartTopTracks> = flow {
    val params = mapOf(
      "limit" to "10",
      "page" to page.toString()
    )
    val chartTopTracksEndpoint = ChartTopTracksEndpoint(
      params = params
    )
    val response = lastFmService.request(chartTopTracksEndpoint)
    emit(response.toChartTopTracks())
  }.flowOn(Dispatchers.IO)

  override fun topTags(page: Int): Flow<ImmutableList<Tag>> = flow {
    val params = mapOf(
      "limit" to "20",
      "page" to page.toString()
    )
    val chartTopTagsEndpoint = ChartTopTagsEndpoint(
      params = params
    )
    val response = lastFmService.request(chartTopTagsEndpoint)
    emit(response.toChartTopTags())
  }.flowOn(Dispatchers.IO)

  override suspend fun topArtistsAsync(page: Int): Result<ChartTopArtists> {
    return withContext(Dispatchers.IO) {
      val params = mapOf(
        "limit" to "10",
        "page" to page.toString()
      )
      val chartTopArtistsEndpoint = ChartTopArtistsEndpoint(
        params = params
      )
      runCatching {
        val response = lastFmService.request(chartTopArtistsEndpoint)
        val topArtists = response.toChartTopArtists().topArtists.map { artist ->
          val imageUrl = artworkDataStore.artwork(
            artist = artist.name
          )
          if (imageUrl != null) {
            artist.imageUrl = imageUrl
          }
          artist
        }
        ChartTopArtists(
          topArtists = topArtists,
          pagingAttr = response.toChartTopArtists().pagingAttr
        )
      }
    }
  }

  override suspend fun topTagsAsync(page: Int): Result<ImmutableList<Tag>> {
    return withContext(Dispatchers.IO) {
      val params = mapOf(
        "limit" to "20",
        "page" to page.toString()
      )
      val chartTopTagsEndpoint = ChartTopTagsEndpoint(
        params = params
      )
      runCatching {
        val response = lastFmService.request(chartTopTagsEndpoint)
        response.toChartTopTags()
      }
    }
  }

  override suspend fun topTracksAsync(page: Int): Result<ChartTopTracks> {
    return withContext(Dispatchers.IO) {
      val params = mapOf(
        "limit" to "10",
        "page" to page.toString()
      )

      val chartTopTracksEndpoint = ChartTopTracksEndpoint(
        params = params
      )
      runCatching {
        val response = lastFmService.request(chartTopTracksEndpoint)
        val result = response.toChartTopTracks().topTracks.map { track ->
          if (track.imageList.imageUrl().isInvalidArtwork()) {
            val imageUrl = artworkDataStore.artwork(
              artist = track.artist.name,
            )
            if (imageUrl != null) {
              track.imageUrl = imageUrl
            }
          }
          track
        }
        ChartTopTracks(
          topTracks = result,
          pagingAttr = response.toChartTopTracks().pagingAttr
        )
      }
    }
  }
}
