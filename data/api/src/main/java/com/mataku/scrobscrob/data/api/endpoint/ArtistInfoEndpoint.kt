package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse

data class ArtistInfoEndpoint(
  override val path: String = "/2.0/?method=artist.getInfo&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, Any?>
) : Endpoint<ArtistInfoApiResponse>
