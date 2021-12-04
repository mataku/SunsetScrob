package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.api.endpoint.MobileSession
import kotlinx.coroutines.flow.Flow

interface MobileSessionRepository {
    suspend fun authorize(userName: String, password: String): Flow<MobileSession>
}