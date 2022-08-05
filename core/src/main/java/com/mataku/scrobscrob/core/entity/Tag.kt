package com.mataku.scrobscrob.core.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
  val name: String,
  val url: String
)

@Serializable
data class TopTags(
  @SerialName("tag")
  val tagList: List<Tag>
)
