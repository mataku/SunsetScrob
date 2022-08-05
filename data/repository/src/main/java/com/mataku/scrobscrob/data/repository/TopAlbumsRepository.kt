package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserTopAlbumsEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toTopAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface TopAlbumsRepository {
  suspend fun fetchTopAlbums(page: Int, username: String): Flow<List<AlbumInfo>>
}

@Singleton
class TopAlbumsRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : TopAlbumsRepository {
  override suspend fun fetchTopAlbums(page: Int, username: String): Flow<List<AlbumInfo>> = flow {
    val params = mapOf(
      "limit" to 20,
      "page" to page,
      "period" to "overall",
      "user" to username
    )
    val endpoint = UserTopAlbumsEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toTopAlbums())
  }.flowOn(Dispatchers.IO)
}
