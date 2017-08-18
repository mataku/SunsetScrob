package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.MobileSessionApiResponse
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
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

    @retrofit2.http.GET("/2.0/?method=track.getInfo&format=json")
    fun getTrackInfo(
            @Query("artist") artist: String,
            @Query("track") track: String,
            @Query("api_key") api_key: String
    ): Call<TrackInfoApiResponse>

    // TODO: multi track scrobbling
    @retrofit2.http.POST("/2.0/?method=track.scrobble&format=json")
    fun scrobble(
            @Query("artist[0]") artist: String,
            @Query("track[0]") track: String,
            @Query("timestamp[0]") timeStamp: Long,
            @Query("album[0]") albumName: String,
            @Query("api_key") apiKey: String,
            @Query("api_sig") apiSig: String,
            @Query("sk") sessionKey: String
    ): Call<ScrobblesApiResponse>
}