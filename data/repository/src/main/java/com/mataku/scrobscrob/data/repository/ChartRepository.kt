package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ChartTopArtistsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ChartTopTagsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ChartTopTracksEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toChartTopArtists
import com.mataku.scrobscrob.data.repository.mapper.toChartTopTags
import com.mataku.scrobscrob.data.repository.mapper.toChartTopTracks
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ChartRepository {
  fun topArtists(page: Int): Flow<ChartTopArtists>

  fun topTracks(page: Int): Flow<ChartTopTracks>

  fun topTags(page: Int): Flow<List<Tag>>
}

@Singleton
class ChartRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : ChartRepository {
  override fun topArtists(page: Int): Flow<ChartTopArtists> = flow {
    val params = mapOf(
      "limit" to "20",
      "page" to page.toString()
    )
    val chartTopArtistsEndpoint = ChartTopArtistsEndpoint(
      params = params
    )
    val response = lastFmService.request(chartTopArtistsEndpoint)
    emit(response.toChartTopArtists())
  }.flowOn(Dispatchers.IO)

  override fun topTracks(page: Int): Flow<ChartTopTracks> = flow {
    val params = mapOf(
      "limit" to "20",
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
  }
}
