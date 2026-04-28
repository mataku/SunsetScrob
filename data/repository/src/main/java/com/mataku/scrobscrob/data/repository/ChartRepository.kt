package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.isInvalidArtwork
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ChartTopArtistsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ChartTopTracksEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.repository.mapper.toChartTopArtists
import com.mataku.scrobscrob.data.repository.mapper.toChartTopTracks
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface ChartRepository {
  fun topArtists(page: Int): Flow<ChartTopArtists>

  fun topTracks(page: Int): Flow<ChartTopTracks>
}

@SingleIn(AppScope::class)
@Inject
class ChartRepositoryImpl(
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
    val chartTopArtists = response.toChartTopArtists()
    val topArtists = chartTopArtists.topArtists.map { artist ->
      val imageUrl = artworkDataStore.artwork(
        artist = artist.name
      )
      if (imageUrl != null) {
        artist.copy(imageUrl = imageUrl)
      } else {
        artist
      }
    }.toImmutableList()

    emit(
      ChartTopArtists(
        topArtists = topArtists,
        pagingAttr = chartTopArtists.pagingAttr
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
    val chartTopTracks = response.toChartTopTracks()
    val topTracks = chartTopTracks.topTracks.map { track ->
      if (track.imageList.imageUrl().isInvalidArtwork()) {
        val imageUrl = artworkDataStore.artwork(
          artist = track.artist.name,
        )
        if (imageUrl != null) {
          track.copy(imageUrl = imageUrl)
        } else {
          track
        }
      } else {
        track
      }
    }.toImmutableList()
    emit(
      ChartTopTracks(
        topTracks = topTracks,
        pagingAttr = chartTopTracks.pagingAttr
      )
    )
  }.flowOn(Dispatchers.IO)
}
