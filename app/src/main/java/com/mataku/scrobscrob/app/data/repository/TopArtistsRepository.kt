package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.core.api.ApiClient
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult

class TopArtistsRepository(private val apiClient: ApiClient) {
    suspend fun topArtistsResponse(
        page: Int,
        userName: String
    ): SunsetResult<List<Artist>> {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to userName
        )

        return try {
            val request = apiClient.get<TopArtistsApiResponse>(TopArtistsEndpoint(params = params))
            if (request.topArtists.artists.isNullOrEmpty()) {
                SunsetResult.success(emptyList())
            } else {
                SunsetResult.success(request.topArtists.artists)
            }
        } catch (e: Exception) {
            SunsetResult.failure(e)
        }
    }
}