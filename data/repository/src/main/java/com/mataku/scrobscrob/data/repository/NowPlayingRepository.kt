package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.NowPlaying
import com.mataku.scrobscrob.core.api.endpoint.NowPlayingApiResponse
import com.mataku.scrobscrob.core.api.endpoint.UpdateNowPlayingEndpoint
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface NowPlayingRepository {
    suspend fun update(trackName: String, artistName: String): Flow<NowPlaying>
}

@Singleton
class NowPlayingRepositoryImpl @Inject constructor(
    private val lastFmService: com.mataku.scrobscrob.data.api.LastFmService,
    private val sessionKeyDataStore: SessionKeyDataStore
) : NowPlayingRepository {
    override suspend fun update(
        trackName: String,
        artistName: String
    ): Flow<NowPlaying> {
        val sessionKey = sessionKeyDataStore.sessionKey()
        return flow {
            if (sessionKey == null) {
                emptyFlow<NowPlaying>()
            } else {
                val params = mutableMapOf(
                    "artist" to artistName,
                    "track" to trackName,
                    "album" to "",
                    "method" to NOW_PLAYING_METHOD,
                    "sk" to sessionKey
                )
                val apiSig = ApiSignature.generateApiSig(params)
                params.remove("method")
                params["api_sig"] = apiSig


                emit(lastFmService.post<NowPlayingApiResponse>(UpdateNowPlayingEndpoint(params = params)).nowPlaying)
            }
        }
    }

    companion object {
        private const val NOW_PLAYING_METHOD = "track.updateNowPlaying"
    }
}