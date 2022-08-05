package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.UserTopArtistsApiResponse
import io.ktor.http.HttpMethod

data class UserTopArtistsEndpoint(
  override val path: String = "/2.0/?method=user.getTopArtists&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any>,
) : Endpoint<UserTopArtistsApiResponse>
