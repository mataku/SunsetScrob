package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.MobileSessionApiResponse
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
}