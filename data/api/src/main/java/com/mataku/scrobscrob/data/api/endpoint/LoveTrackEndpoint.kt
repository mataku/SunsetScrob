package com.mataku.scrobscrob.data.api.endpoint

class LoveTrackEndpoint(
  override val path: String = "/2.0/?method=track.love&format=json",
  override val requestType: HttpMethod = HttpMethod.POST,
  override val params: Map<String, String>
) : Endpoint<Unit> {
  companion object {
    const val METHOD = "track.love"
  }
}
