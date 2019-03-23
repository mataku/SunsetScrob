package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.api.service.UserTopArtistsService
import com.mataku.scrobscrob.core.entity.Artist
import com.mataku.scrobscrob.core.entity.presentation.Result
import java.io.IOException
import javax.net.ssl.HttpsURLConnection

class TopArtistsRepository(val topArtistsService: UserTopArtistsService) {
    suspend fun topArtistsResponse(page: Int, userName: String): Result<List<Artist>> {
        return try {
            val result = topArtistsService
                .getTopArtists(20, page, "overall", userName).await()
            when (result.code()) {
                HttpsURLConnection.HTTP_OK -> {
                    result.body()?.topArtists?.let {
                        Result.success(it.artists)
                    } ?: Result.success(emptyList())
                }
                else -> {
                    Result.failure(R.string.error_message_fetch_top_artists)
                }
            }
        } catch (e: IOException) {
            Result.failure(R.string.error_message_bad_network)
        }
    }
}