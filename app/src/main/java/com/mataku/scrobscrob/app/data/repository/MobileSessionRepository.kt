package com.mataku.scrobscrob.app.data.repository

import com.mataku.scrobscrob.app.util.AppUtil
import com.mataku.scrobscrob.core.api.ApiClient
import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionApiResponse
import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionEndpoint
import com.mataku.scrobscrob.core.api.endpoint.MobileSession
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult

class MobileSessionRepository(private val apiClient: ApiClient) {
    suspend fun authorize(userName: String, password: String): SunsetResult<MobileSession> {
        val params = mutableMapOf(
            "username" to userName,
            "password" to password,
            "method" to METHOD
        )
        val apiSig = AppUtil.generateApiSig(params)
        params.remove("method")
        params["api_sig"] = apiSig
        return try {
            val result = apiClient.post<AuthMobileSessionApiResponse>(AuthMobileSessionEndpoint(params = params))
            result.mobileSession?.let {
                SunsetResult.success(it)
            } ?: SunsetResult.failure(Throwable())
        } catch (e: Exception) {
            SunsetResult.failure(e)
        }
    }

    companion object {
        private const val METHOD = "auth.getMobileSession"
    }
}