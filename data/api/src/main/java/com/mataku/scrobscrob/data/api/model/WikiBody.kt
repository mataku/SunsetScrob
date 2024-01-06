package com.mataku.scrobscrob.data.api.model

import com.mataku.scrobscrob.data.api.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class WikiBody(
  @Serializable(DateSerializer::class)
  val published: Date,

  @SerialName("summary")
  val summary: String? = null,

  @SerialName("content")
  val content: String? = null
)
