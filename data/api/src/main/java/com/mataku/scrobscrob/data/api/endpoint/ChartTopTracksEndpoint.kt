package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ChartTopTracksResponse

data class ChartTopTracksEndpoint(
  override val path: String = "/2.0/?method=chart.gettoptracks&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, String>
) : Endpoint<ChartTopTracksResponse>
