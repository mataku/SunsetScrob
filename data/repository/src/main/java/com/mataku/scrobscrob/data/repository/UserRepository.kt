package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserInfoEndpoint
import com.mataku.scrobscrob.data.repository.mapper.toUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
  suspend fun getInfo(userName: String): Flow<UserInfo>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : UserRepository {
  override suspend fun getInfo(userName: String): Flow<UserInfo> = flow {
    val endpoint = UserInfoEndpoint(
      params = mapOf(
        "user" to userName
      )
    )
    val userInfo = lastFmService.request(endpoint).toUserInfo()
    emit(userInfo)
  }
}
