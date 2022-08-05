package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import io.ktor.http.HttpMethod

data class UserRecentTracksEndpoint(
  override val path: String = "/2.0/?method=user.getrecenttracks&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any>
) : Endpoint<RecentTracksApiResponse>
