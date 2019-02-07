package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopArtistsService
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import javax.net.ssl.HttpsURLConnection

class TopArtistsRepository {
    suspend fun topArtistsResponse(page: Int, userName: String): Result<List<Artist>> {
        val result = LastFmApiClient.create(UserTopArtistsService::class.java)
            .getTopArtists(20, page, "overall", userName).await()
        return when (result.code()) {
            HttpsURLConnection.HTTP_OK -> {
                result.body()?.topArtists?.let {
                    Result.success(it.artists)
                } ?: Result.success(emptyList())
            }
            else -> {
                val error = Result.ErrorResponse(1, "Failed to get top albums")
                Result.failure(error)
            }
        }
    }
}