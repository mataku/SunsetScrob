package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.TopAlbumsApiResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserTopAlbumsService {
    @GET("/2.0/?method=user.getTopAlbums&format=json")
    fun getTopAlbum(
            @Query("limit") limit: Int,
            @Query("page") page: Int,
            @Query("period") period: String,
            @Query("user") user: String
    ): Deferred<Response<TopAlbumsApiResponse>>
}