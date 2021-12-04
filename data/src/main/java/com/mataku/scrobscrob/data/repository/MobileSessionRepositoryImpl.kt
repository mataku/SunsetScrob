package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionApiResponse
import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionEndpoint
import com.mataku.scrobscrob.core.api.endpoint.MobileSession
import com.mataku.scrobscrob.data.api.LastFmService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MobileSessionRepositoryImpl @Inject constructor(
    private val lastFmService: LastFmService
) : MobileSessionRepository {
    override suspend fun authorize(userName: String, password: String): Flow<MobileSession> {
        val params = mutableMapOf(
            "username" to userName,
            "password" to password,
            "method" to METHOD
        )
        val apiSig = ApiSignature.generateApiSig(params)
        params["api_sig"] = apiSig
        return flow {
            val result =
                lastFmService.post<AuthMobileSessionApiResponse>(AuthMobileSessionEndpoint(params = params))
            emit(result.mobileSession)
        }
    }

    companion object {
        private const val METHOD = "auth.getMobileSession"
    }
}