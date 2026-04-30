package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse

data class UserRecentTracksEndpoint(
  override val path: String = "/2.0/?method=user.getrecenttracks&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, Any>
) : Endpoint<RecentTracksApiResponse>
