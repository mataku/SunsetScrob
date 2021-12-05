package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.MobileSession
import com.mataku.scrobscrob.data.api.LastFmService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface MobileSessionRepository {
    suspend fun authorize(userName: String, password: String): Flow<MobileSession>
}

@Singleton
class MobileSessionRepositoryImpl @Inject constructor(private val lastFmService: LastFmService) :
    MobileSessionRepository {
    override suspend fun authorize(userName: String, password: String): Flow<MobileSession> {
        val params = mutableMapOf(
            "username" to userName,
            "password" to password,
            "method" to METHOD
        )
        val apiSig = ApiSignature.generateApiSig(params)
        params["api_sig"] = apiSig
        return flow {
            emit(MobileSession("", ""))
        }
    }

    companion object {
        private const val METHOD = "auth.getMobileSession"
    }
}