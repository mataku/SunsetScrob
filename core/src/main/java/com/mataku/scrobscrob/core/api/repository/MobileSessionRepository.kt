package com.mataku.scrobscrob.core.api.repository

import android.util.Log
import com.mataku.scrobscrob.core.api.LastFmApiClient
import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionApiResponse
import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionEndpoint
import com.mataku.scrobscrob.core.api.endpoint.MobileSession
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import com.mataku.scrobscrob.core.util.AppUtil

class MobileSessionRepository(private val apiClient: LastFmApiClient) {
    suspend fun authorize(userName: String, password: String): SunsetResult<MobileSession> {
        val params = mutableMapOf(
            "username" to userName,
            "password" to password,
            "method" to METHOD
        )
        val apiSig = AppUtil().generateApiSig(params)
        params["api_sig"] = apiSig

        return try {
            val result =
                apiClient.post<AuthMobileSessionApiResponse>(AuthMobileSessionEndpoint(params = params))
            result.mobileSession?.let {
                SunsetResult.success(it)
            } ?: SunsetResult.failure(Throwable())
        } catch (e: Exception) {
            Log.i("MATAKUDEBUG", e.javaClass.toString())
            Log.i("MATAKUDEBUG", e.localizedMessage)
            SunsetResult.failure(e)
        }
    }

    companion object {
        private const val METHOD = "auth.getMobileSession"
    }
}