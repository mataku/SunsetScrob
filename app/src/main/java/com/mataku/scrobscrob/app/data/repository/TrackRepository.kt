package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.core.api.LastFmApiClient
import com.mataku.scrobscrob.core.api.endpoint.TrackInfo
import com.mataku.scrobscrob.core.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.core.api.endpoint.TrackInfoEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult

class TrackRepository(private val apiClient: LastFmApiClient) {
    suspend fun getInfo(trackName: String, artistName: String): SunsetResult<TrackInfo> {
        val params = mapOf(
            "artist" to artistName,
            "track" to trackName
        )

        return try {
            val request = apiClient.get<TrackInfoApiResponse>(TrackInfoEndpoint(params = params))
            request.trackInfo?.let {
                SunsetResult.success(it)
            } ?: SunsetResult.failure(Throwable())
        } catch (e: Exception) {
            SunsetResult.failure(e)
        }
    }
}