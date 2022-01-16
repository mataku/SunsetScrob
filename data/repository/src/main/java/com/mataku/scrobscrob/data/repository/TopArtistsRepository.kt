package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsEndpoint
import com.mataku.scrobscrob.data.api.LastFmService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface TopArtistsRepository {
    suspend fun fetchTopArtists(page: Int, username: String): Flow<List<Artist>>
}

@Singleton
class TopArtistsRepositoryImpl @Inject constructor(
    private val lastFmService: LastFmService
) : TopArtistsRepository {
    override suspend fun fetchTopArtists(page: Int, username: String): Flow<List<Artist>> = flow {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to username
        )
        val artists = lastFmService.get<TopArtistsApiResponse>(
            TopArtistsEndpoint(params = params)
        ).topArtists.artists
        emit(artists)
    }.flowOn(Dispatchers.IO)
}