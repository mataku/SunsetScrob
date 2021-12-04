package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.NowPlaying
import kotlinx.coroutines.flow.Flow

interface NowPlayingRepository {
    suspend fun update(trackName: String, artistName: String): Flow<NowPlaying>
}