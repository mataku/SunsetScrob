package com.mataku.scrobscrob.core.api.repository

import android.util.Log
import com.mataku.scrobscrob.core.api.LastFmApiClient
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TopArtistsEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult

class TopArtistsRepository(private val apiClient: LastFmApiClient) {
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
            Log.i("MATAKUDEBUG", e.javaClass.toString())
            Log.i("MATAKUDEBUG", e.localizedMessage)
            SunsetResult.failure(e)
        }
    }
}