package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.AlbumInfoResponse

data class AlbumInfoEndpoint(
  override val path: String = "/2.0/?method=album.getinfo&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, String>
) : Endpoint<AlbumInfoResponse>
