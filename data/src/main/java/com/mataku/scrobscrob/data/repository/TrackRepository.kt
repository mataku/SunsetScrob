package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.TrackInfo
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun getInfo(trackName: String, artistName: String): Flow<TrackInfo>
}