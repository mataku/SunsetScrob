package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbums
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.request
import com.mataku.scrobscrob.data.api.endpoint.AlbumInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserTopAlbumsEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toAlbumInfo
import com.mataku.scrobscrob.data.repository.mapper.toTopAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface AlbumRepository {
  suspend fun fetchTopAlbums(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<TopAlbums>

  fun albumInfo(
    albumName: String,
    artistName: String,
  ): Flow<AlbumInfo>
}

@SingleIn(AppScope::class)
@Inject
class AlbumRepositoryImpl(
  private val lastFmService: LastFmService
) : AlbumRepository {
  override suspend fun fetchTopAlbums(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<TopAlbums> = flow {
    val params = mapOf(
      "limit" to 20,
      "page" to page,
      "period" to timeRangeFiltering.rawValue,
      "user" to username
    )
    val endpoint = UserTopAlbumsEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toTopAlbums())
  }.flowOn(Dispatchers.IO)

  override fun albumInfo(
    albumName: String,
    artistName: String
  ): Flow<AlbumInfo> = flow<AlbumInfo> {
    val params = mapOf(
      "album" to albumName,
      "artist" to artistName
    )
    val endpoint = AlbumInfoEndpoint(params = params)
    val response = lastFmService.request(endpoint)
    emit(response.albumInfoBody.toAlbumInfo())
  }.flowOn(Dispatchers.IO)
}
