package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TrackUpdateNowPlayingService {
    @FormUrlEncoded
    @POST("/2.0/?method=track.updateNowPlaying&format=json")
    fun updateNowPlaying(
            @Field("artist") artist: String,
            @Field("track") trackName: String,
            @Field("album") albumName: String,
            @Field("api_key") apiKey: String,
            @Field("api_sig") apiSig: String,
            @Field("sk") sessionKey: String
    ): Call<NowPlayingApiResponse>
}