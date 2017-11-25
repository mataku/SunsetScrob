package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackInfoService {
    @GET("/2.0/?method=track.getInfo&format=json")
    fun getTrackInfo(
            @Query("artist") artist: String,
            @Query("track") track: String,
            @Query("api_key") api_key: String
    ): Call<TrackInfoApiResponse>
}