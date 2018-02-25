package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.model.entity.TopAlbumsApiResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserTopAlbumsService {
    @GET("/2.0/?method=user.getTopAlbums&format=json")
    fun getTopAlbum(
            @Query("user") user: String,
            @Query("period") period: String
    ) : Single<Response<TopAlbumsApiResponse>>
}