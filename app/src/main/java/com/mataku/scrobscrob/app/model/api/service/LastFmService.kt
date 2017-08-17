package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.MobileSessionApiResponse
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import retrofit2.Call
import retrofit2.http.Query

interface LastFmService {
    @retrofit2.http.POST("/2.0/?method=auth.getMobileSession")
    fun authenticate(
            @Query("username") userName: String,
            @Query("password") password: String,
            @Query("api_key") apiKey: String,
            @Query("api_sig") apiSig: String,
            @Query("format") format: String
    ): Call<MobileSessionApiResponse>

    @retrofit2.http.POST("/2.0/?method=track.updateNowPlaying&format=json")
    fun updateNowPlaying(
            @Query("artist") artist: String,
            @Query("track") trackName: String,
            @Query("album") albumName: String,
            @Query("api_key") apiKey: String,
            @Query("api_sig") apiSig: String,
            @Query("sk") sessionKey: String
    ): Call<NowPlayingApiResponse>
}