package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ArtistInfoEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toArtistInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ArtistRepository {
  suspend fun artistInfo(
    name: String
  ): Flow<ArtistInfo>
}

class ArtistRepositoryImpl(
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
  }
}
