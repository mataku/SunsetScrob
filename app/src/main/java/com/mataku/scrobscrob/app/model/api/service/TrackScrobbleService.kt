package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface TrackScrobbleService {
    // TODO: multi track scrobbling
    @FormUrlEncoded
    @retrofit2.http.POST("/2.0/?method=track.scrobble&format=json")
    fun scrobble(
            @Field("artist[0]") artist: String,
            @Field("track[0]") track: String,
            @Field("timestamp[0]") timeStamp: Long,
            @Field("album[0]") albumName: String,
            @Field("api_sig") apiSig: String,
            @Field("sk") sessionKey: String
    ): Single<Response<ScrobblesApiResponse>>
}