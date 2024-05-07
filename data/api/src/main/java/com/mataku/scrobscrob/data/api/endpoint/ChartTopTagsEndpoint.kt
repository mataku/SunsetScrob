package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.ChartTopTagsResponse
import io.ktor.http.HttpMethod

data class ChartTopTagsEndpoint(
  override val path: String = "/2.0/?method=chart.gettoptags&format=json",
  override val params: Map<String, Any>,
  override val requestType: HttpMethod = HttpMethod.Get
) : Endpoint<ChartTopTagsResponse>
