package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionApiResponse
import com.mataku.scrobscrob.core.api.endpoint.AuthMobileSessionEndpoint
import com.mataku.scrobscrob.core.api.endpoint.MobileSession
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface MobileSessionRepository {
    suspend fun authorize(userName: String, password: String): Flow<MobileSession>
}

@Singleton
class MobileSessionRepositoryImpl @Inject constructor(
    private val lastFmService: LastFmService,
    private val sessionKeyDataStore: SessionKeyDataStore,
    private val usernameDataStore: UsernameDataStore
) :
    MobileSessionRepository {
    override suspend fun authorize(userName: String, password: String): Flow<MobileSession> = flow {
        val params = mutableMapOf(
            "username" to userName,
            "password" to password,
            "method" to METHOD
        )
        val apiSig = ApiSignature.generateApiSig(params)
        params["api_sig"] = apiSig
        val result = lastFmService.post<AuthMobileSessionApiResponse>(
            AuthMobileSessionEndpoint(
                params = params
            )
        ).mobileSession
        sessionKeyDataStore.setSessionKey(result.key)
        usernameDataStore.setUsername(result.name)
        emit(result)
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val METHOD = "auth.getMobileSession"
    }
}