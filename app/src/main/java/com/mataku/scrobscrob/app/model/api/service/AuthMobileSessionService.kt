package com.mataku.scrobscrob.app.model.api.service

import com.mataku.scrobscrob.core.entity.MobileSessionApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthMobileSessionService {
    @POST("/2.0/?method=auth.getMobileSession&format=json")
    fun auth(
        @Query("username") userName: String,
        @Query("password") password: String,
        @Query("api_sig") apiSig: String
    ): Deferred<Response<MobileSessionApiResponse>>
}