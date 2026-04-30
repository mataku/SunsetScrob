package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.UserInfoApiResponse

data class UserInfoEndpoint(
  override val path: String = "/2.0/?method=user.getinfo&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, Any>,
) : Endpoint<UserInfoApiResponse>
