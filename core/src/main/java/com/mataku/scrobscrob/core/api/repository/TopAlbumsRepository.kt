package com.mataku.scrobscrob.core.api.repository

import com.mataku.scrobscrob.core.api.LastFmApiClient
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.core.api.endpoint.TopAlbumsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopAlbumsEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TopAlbumsRepository(private val apiClient: LastFmApiClient) {
    suspend fun topAlbumsResponse(page: Int, userName: String): Flow<SunsetResult<List<Album>>> {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to userName
        )

        return apiClient.getAsFlow<TopAlbumsApiResponse>(TopAlbumsEndpoint(params = params))
            .map {
                val response = it.topAlbums.albums
                if (response.isNullOrEmpty()) {
                    SunsetResult.success(emptyList())
                } else {
                    SunsetResult.success(response)
                }
            }
            .catch {
                SunsetResult.failure<List<Album>>(it)
            }
    }
}