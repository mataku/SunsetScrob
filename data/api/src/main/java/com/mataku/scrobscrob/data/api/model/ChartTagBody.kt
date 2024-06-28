package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChartTagBody(
  val name: String,
  val url: String,
)

@Serializable
data class ChartTopTagsResponse(
  @SerialName("tags")
  val chartTopTagsBody: ChartTopTagsBody
)

@Serializable
data class ChartTopTagsBody(
  @SerialName("tag")
  val tagList: List<ChartTagBody>
)
