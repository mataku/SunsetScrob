package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ArtistInfoEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toArtistInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ArtistRepository {
  suspend fun artistInfo(
    name: String
  ): Flow<ArtistInfo>
}

@Singleton
class ArtistRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : ArtistRepository {
  override suspend fun artistInfo(name: String): Flow<ArtistInfo> = flow {
    val params = mapOf(
      "artist" to name
    )
    val endpoint = ArtistInfoEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toArtistInfo())
  }.flowOn(Dispatchers.IO)
}
