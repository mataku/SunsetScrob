package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.app.model.entity.MobileSessionApiResponse
import retrofit2.Call
import retrofit2.http.Query

interface AuthMobileSessionService {
    @retrofit2.http.POST("/2.0/?method=auth.getMobileSession&format=json")
    fun authenticate(
            @Query("username") userName: String,
            @Query("password") password: String,
            @Query("api_sig") apiSig: String
    ): Call<MobileSessionApiResponse>
}