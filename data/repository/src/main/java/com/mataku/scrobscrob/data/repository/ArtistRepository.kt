package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ArtistInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ArtistTopAlbumsEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toArtistInfo
import com.mataku.scrobscrob.data.repository.mapper.toTopAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface ArtistRepository {
  fun artistInfo(
    name: String
  ): Flow<ArtistInfo>

  fun topAlbums(
    name: String,
    page: Int,
    limit: Int
  ): Flow<List<TopAlbumInfo>>
}

@SingleIn(AppScope::class)
@Inject
class ArtistRepositoryImpl(
  private val lastFmService: LastFmService
) : ArtistRepository {
  override fun artistInfo(name: String): Flow<ArtistInfo> = flow {
    val params = mapOf(
      "artist" to name
    )
    val endpoint = ArtistInfoEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toArtistInfo())
  }.flowOn(Dispatchers.IO)

  override fun topAlbums(name: String, page: Int, limit: Int): Flow<List<TopAlbumInfo>> = flow {
    val params = mapOf(
      "artist" to name,
      "page" to page.toString(),
      "limit" to limit.toString()
    )
    val endpoint = ArtistTopAlbumsEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toTopAlbums().albums)
  }.flowOn(Dispatchers.IO)
}
