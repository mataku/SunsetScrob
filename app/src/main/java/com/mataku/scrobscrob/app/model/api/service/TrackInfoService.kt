package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackInfoService {
    @GET("/2.0/?method=track.getInfo&format=json")
    fun getTrackInfo(
        @Query("artist") artist: String,
        @Query("track") track: String
    ): Deferred<Response<TrackInfoApiResponse>>
}