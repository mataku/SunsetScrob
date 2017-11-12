package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.AlbumInfoApiResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumInfoService {
    @GET("/2.0/?method=album.getInfo&format=json")
    fun getAlbumInfo(
            @Query("album") album: String,
            @Query("artist") artist: String,
            @Query("track") track: String,
            @Query("api_key") api_key: String
    ): Single<Response<AlbumInfoApiResponse>>
}