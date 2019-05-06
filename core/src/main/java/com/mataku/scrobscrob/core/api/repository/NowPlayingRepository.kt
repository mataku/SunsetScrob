package com.mataku.scrobscrob.core.api.repository

import com.mataku.scrobscrob.core.api.LastFmApiClient
import com.mataku.scrobscrob.core.api.endpoint.NowPlaying
import com.mataku.scrobscrob.core.api.endpoint.NowPlayingApiResponse
import com.mataku.scrobscrob.core.api.endpoint.UpdateNowPlayingEndpoint
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import com.mataku.scrobscrob.core.util.AppUtil

class NowPlayingRepository(private val apiClient: LastFmApiClient) {
    suspend fun update(trackName: String, artistName: String, sessionKey: String): SunsetResult<NowPlaying> {
        val params = mutableMapOf(
            "artist" to artistName,
            "track" to trackName,
            "album" to "",
            "method" to NOW_PLAYING_METHOD,
            "sk" to sessionKey
        )
        val apiSig = AppUtil.generateApiSig(params)
        params.remove("method")
        params["api_sig"] = apiSig

        return try {
            val request = apiClient.post<NowPlayingApiResponse>(UpdateNowPlayingEndpoint(params = params))
            val nowPlaying = request.nowPlaying
            nowPlaying?.let {
                SunsetResult.success(it)
            } ?: SunsetResult.failure(Throwable())
        } catch (e: Exception) {
            SunsetResult.failure(e)
        }
    }

    companion object {
        private const val NOW_PLAYING_METHOD = "track.updateNowPlaying"
    }
}