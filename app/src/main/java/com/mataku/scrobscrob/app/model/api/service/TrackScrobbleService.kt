package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TrackScrobbleService {
    // TODO: multi track scrobbling
    @FormUrlEncoded
    @POST("/2.0/?method=track.scrobble&format=json")
    fun scrobble(
            @Field("artist[0]") artist: String,
            @Field("track[0]") track: String,
            @Field("timestamp[0]") timeStamp: Long,
            @Field("album[0]") albumName: String,
            @Field("api_key") apiKey: String,
            @Field("api_sig") apiSig: String,
            @Field("sk") sessionKey: String
    ): Call<ScrobblesApiResponse>
}