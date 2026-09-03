package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ScrobbleApiResponse

data class ScrobbleEndpoint(
  override val path: String = "/2.0/?method=track.scrobble&format=json",
  override val requestType: HttpMethod = HttpMethod.POST,
  override val params: Map<String, Any>,
) : Endpoint<ScrobbleApiResponse>
