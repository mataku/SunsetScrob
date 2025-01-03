package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.LovedTracksResponse
import io.ktor.http.HttpMethod

data class UserLovedTracksEndpoint(
  override val path: String = "/2.0/?method=user.getlovedtracks&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, String>
) : Endpoint<LovedTracksResponse>
