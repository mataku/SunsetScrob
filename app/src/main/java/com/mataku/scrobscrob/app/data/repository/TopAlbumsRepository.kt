package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import javax.net.ssl.HttpsURLConnection

class TopAlbumsRepository {
    suspend fun topAlbumsResponse(page: Int, userName: String): Result<List<Album>> {
        val result = LastFmApiClient.create(UserTopAlbumsService::class.java)
            .getTopAlbum(20, page, "overall", userName).await()
        return when (result.code()) {
            HttpsURLConnection.HTTP_OK -> {
                result.body()?.topAlbums?.let {
                    Result.success(it.albums)
                } ?: Result.success(emptyList())
            }
            else -> {
                val error = Result.ErrorResponse(1, "Failed to get top albums")
                Result.failure(error)
            }
        }
    }
}