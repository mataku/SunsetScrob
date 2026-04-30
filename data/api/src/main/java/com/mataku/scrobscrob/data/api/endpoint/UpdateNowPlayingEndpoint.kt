package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.NowPlayingApiResponse

data class UpdateNowPlayingEndpoint(
  override val path: String = "/2.0/?method=track.updateNowPlaying&format=json",
  override val params: Map<String, Any>,
  override val requestType: HttpMethod = HttpMethod.POST
) : Endpoint<NowPlayingApiResponse>
