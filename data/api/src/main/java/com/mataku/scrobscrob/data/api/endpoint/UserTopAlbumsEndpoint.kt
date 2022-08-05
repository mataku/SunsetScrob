package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.UserTopAlbumsApiResponse
import io.ktor.http.HttpMethod

data class UserTopAlbumsEndpoint(
  override val path: String = "/2.0/?method=user.getTopAlbums&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any>
) : Endpoint<UserTopAlbumsApiResponse>
