package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.ArtistInfo
import com.mataku.scrobscrob.core.api.endpoint.ArtistInfoApiResponse
import com.mataku.scrobscrob.core.api.endpoint.ArtistInfoEndpoint
import com.mataku.scrobscrob.data.api.LastFmService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    emit(
      lastFmService.get<ArtistInfoApiResponse>(ArtistInfoEndpoint(params = params)).artistInfo
    )
  }
}
