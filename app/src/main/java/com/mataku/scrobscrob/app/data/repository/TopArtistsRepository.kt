package com.mataku.scrobscrob.app.data.repository

import android.util.Log
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.core.api.ApiClient
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsEndpoint
import com.mataku.scrobscrob.core.entity.presentation.Result
import io.ktor.client.features.BadResponseStatusException

class TopArtistsRepository(val apiClient: ApiClient) {
    suspend fun topArtistsResponse(
        page: Int,
        userName: String
    ): Result<List<com.mataku.scrobscrob.core.api.endpoint.Artist>> {
        val params = mapOf(
            "limit" to 20,
            "page" to page,
            "period" to "overall",
            "user" to userName
        )

        return try {
            val request = apiClient.request<TopArtistsApiResponse>(TopArtistsEndpoint(params = params))
            if (request != null) {
                Result.success(request.artists)
            } else {
                Result.success(emptyList())
            }
        } catch (e: BadResponseStatusException) {
            Log.i("MATAKUDEBUG", e.toString())
            Result.failure(R.string.error_message_fetch_top_artists)
        }
    }
}