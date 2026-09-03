package com.mataku.scrobscrob.data.api.endpoint

class UnLoveTrackEndpoint(
  override val path: String = "/2.0/?method=track.unlove&format=json",
  override val requestType: HttpMethod = HttpMethod.POST,
  override val params: Map<String, String>
) : Endpoint<Unit> {
  companion object {
    const val METHOD = "track.unlove"
  }
}
