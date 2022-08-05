package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumInfoBody(
  @SerialName("artist")
  val artist: String,

  @SerialName("title")
  val title: String,

  @SerialName("image")
  val imageList: List<ImageBody>
)
