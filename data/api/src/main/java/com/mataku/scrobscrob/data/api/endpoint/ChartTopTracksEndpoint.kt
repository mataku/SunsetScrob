package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ChartTopTracksResponse
import io.ktor.http.HttpMethod

data class ChartTopTracksEndpoint(
  override val path: String = "/2.0/?method=chart.gettoptracks&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, String>
) : Endpoint<ChartTopTracksResponse>
