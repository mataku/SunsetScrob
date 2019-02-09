package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import java.io.IOException
import javax.net.ssl.HttpsURLConnection

class TopAlbumsRepository {
    suspend fun topAlbumsResponse(page: Int, userName: String): Result<List<Album>> {
        return try {
            val result = LastFmApiClient.create(UserTopAlbumsService::class.java)
                .getTopAlbum(20, page, "overall", userName).await()
            when (result.code()) {
                HttpsURLConnection.HTTP_OK -> {
                    result.body()?.topAlbums?.let {
                        Result.success(it.albums)
                    } ?: Result.success(emptyList())
                }
                else -> {
                    Result.failure(R.string.error_message_fetch_top_albums)
                }
            }
        } catch (e: IOException) {
            Result.failure(R.string.error_message_bad_network)
        }
    }
}