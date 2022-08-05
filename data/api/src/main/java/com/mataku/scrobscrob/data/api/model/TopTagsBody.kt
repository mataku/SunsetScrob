package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopTagsBody(
  @SerialName("tag")
  val tagList: List<TagBody>
)

@Serializable
data class TagBody(
  val name: String,
  val url: String
)
