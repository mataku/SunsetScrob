package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackInfoBody(
  @SerialName("duration")
  val duration: String? = null,
  @SerialName("album")
  val album: TrackAlbumInfoBody? = null,
  @SerialName("listeners")
  val listeners: String,
  @SerialName("url")
  val url: String,
  @SerialName("toptags")
  val topTags: TopTagsBody
)

@Serializable
data class TrackAlbumInfoBody(
  @SerialName("artist")
  val artist: String,

  @SerialName("title")
  val title: String,

  @SerialName("image")
  val imageList: List<ImageBody>
)

