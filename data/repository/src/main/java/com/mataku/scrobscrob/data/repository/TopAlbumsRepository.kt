package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.core.api.endpoint.TopAlbumsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopAlbumsEndpoint
import com.mataku.scrobscrob.data.api.LastFmService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface TopAlbumsRepository {
    suspend fun fetchTopAlbums(page: Int, username: String): Flow<List<Album>>
}

@Singleton
class TopAlbumsRepositoryImpl @Inject constructor(
    private val lastFmService: LastFmService
) : TopAlbumsRepository {
    override suspend fun fetchTopAlbums(page: Int, username: String): Flow<List<Album>> = flow {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to username
        )
        val albums = lastFmService.get<TopAlbumsApiResponse>(
            TopAlbumsEndpoint(params = params)
        ).topAlbums.albums
        emit(albums)
    }
}