package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageBody(
  @SerialName("size")
  val size: String,

  @SerialName("#text")
  val url: String
)
