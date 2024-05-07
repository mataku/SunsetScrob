package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.UserInfoApiResponse
import io.ktor.http.HttpMethod

data class UserInfoEndpoint(
  override val path: String = "/2.0/?method=user.getinfo&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any>,
) : Endpoint<UserInfoApiResponse>
