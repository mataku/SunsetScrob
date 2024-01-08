package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse
import io.ktor.http.HttpMethod

data class ArtistTopAlbumsEndpoint(
  override val path: String = "/2.0/?method=artist.gettopalbums&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, String>
) : Endpoint<TopAlbumsApiResponse>
