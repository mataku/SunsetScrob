package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackInfoBody(
  // milliseconds
  @SerialName("duration")
  val duration: Long = 0L,
  @SerialName("album")
  val album: TrackAlbumInfoBody? = null,
  @SerialName("listeners")
  val listeners: String,
  @SerialName("url")
  val url: String,
  @SerialName("toptags")
  val topTags: TopTagsBody,
  @SerialName("artist")
  val artist: TrackArtistBody,
  @SerialName("name")
  val name: String
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

@Serializable
data class TrackArtistBody(
  val name: String,
  val url: String
)

