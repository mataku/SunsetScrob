package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.core.api.ApiClient
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsEndpoint
import com.mataku.scrobscrob.core.entity.presentation.Result

class TopArtistsRepository(val apiClient: ApiClient) {
    suspend fun topArtistsResponse(
        page: Int,
        userName: String
    ): Result<List<Artist>> {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to userName
        )

        return try {
            val request = apiClient.get<TopArtistsApiResponse>(TopArtistsEndpoint(params = params))
            if (request.topArtists.artists.isNullOrEmpty()) {
                Result.success(emptyList())
            } else {
                Result.success(request.topArtists.artists)
            }
        } catch (e: Exception) {
            Result.failure(R.string.error_message_fetch_top_artists)
        }
    }
}