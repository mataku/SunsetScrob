package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse

data class ArtistTopAlbumsEndpoint(
  override val path: String = "/2.0/?method=artist.gettopalbums&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, String>
) : Endpoint<TopAlbumsApiResponse>
