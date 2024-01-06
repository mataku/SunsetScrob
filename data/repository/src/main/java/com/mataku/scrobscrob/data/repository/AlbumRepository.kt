package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.AlbumInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserTopAlbumsEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toAlbumInfo
import com.mataku.scrobscrob.data.repository.mapper.toTopAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface AlbumRepository {
  suspend fun fetchTopAlbums(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopAlbumInfo>>

  fun albumInfo(
    albumName: String,
    artistName: String,
  ): Flow<AlbumInfo>
}

@Singleton
class AlbumRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : AlbumRepository {
  override suspend fun fetchTopAlbums(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopAlbumInfo>> = flow {
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
