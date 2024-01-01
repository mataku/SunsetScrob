package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ChartTopArtistsResponse
import io.ktor.http.HttpMethod

data class ChartTopArtistsEndpoint(
  override val path: String = "/2.0/?method=chart.gettopartists&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, String>
) : Endpoint<ChartTopArtistsResponse>
