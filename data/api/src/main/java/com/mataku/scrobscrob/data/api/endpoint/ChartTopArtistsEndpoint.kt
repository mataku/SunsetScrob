package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ChartTopArtistsResponse

data class ChartTopArtistsEndpoint(
  override val path: String = "/2.0/?method=chart.gettopartists&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, String>
) : Endpoint<ChartTopArtistsResponse>
