package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ScrobbleApiResponse
import io.ktor.http.HttpMethod

data class ScrobbleEndpoint(
  override val path: String = "/2.0/?method=track.scrobble&format=json",
  override val requestType: HttpMethod = HttpMethod.Post,
  override val params: Map<String, Any>,
) : Endpoint<ScrobbleApiResponse>
