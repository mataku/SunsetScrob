package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagsBody(
  @SerialName("tag")
  val tagList: List<TagBody> = emptyList()
)

@Serializable
data class TagBody(
  val name: String,
  val url: String
)
