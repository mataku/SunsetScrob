package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse

data class UserTopAlbumsEndpoint(
  override val path: String = "/2.0/?method=user.getTopAlbums&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, Any>
) : Endpoint<TopAlbumsApiResponse>
