package com.mataku.scrobscrob.core.api.repository

import com.mataku.scrobscrob.core.api.LastFmApiClient
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.core.api.endpoint.TopAlbumsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopAlbumsEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult

class TopAlbumsRepository(private val apiClient: LastFmApiClient) {
    suspend fun topAlbumsResponse(page: Int, userName: String): SunsetResult<List<Album>> {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to userName
        )

        return try {
            val request = apiClient.get<TopAlbumsApiResponse>(TopAlbumsEndpoint(params = params))
            val albums = request.topAlbums.albums
            if (request.topAlbums.albums.isNullOrEmpty()) {
                SunsetResult.success(emptyList())
            } else {
                SunsetResult.success(albums)
            }
        } catch (e: Throwable) {
            SunsetResult.failure(e)
        }

    }
}