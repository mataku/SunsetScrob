package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse
import io.ktor.http.HttpMethod

data class ArtistInfoEndpoint(
  override val path: String = "/2.0/?method=artist.getInfo&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any?>
) : Endpoint<ArtistInfoApiResponse>
