package com.mataku.scrobscrob.data.api.model

import com.mataku.scrobscrob.data.api.serializer.TagListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagsBody(
  @SerialName("tag")
  @Serializable(TagListSerializer::class)
  val tagList: List<TagBody> = emptyList()
)

@Serializable
data class TagBody(
  val name: String,
  val url: String
)
