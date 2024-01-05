package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumInfoResponse(
  @SerialName("album")
  val albumInfoBody: AlbumInfoBody
)

@Serializable
data class AlbumInfoBody(
  @SerialName("name")
  val albumName: String,

  @SerialName("artist")
  val artistName: String,

  @SerialName("url")
  val url: String,

  @SerialName("image")
  val images: List<ImageBody>,

  @SerialName("listeners")
  val listeners: String,

  @SerialName("playcount")
  val playCount: String,

  @SerialName("tracks")
  val tracks: AlbumInfoTrackBody
)

@Serializable
data class AlbumInfoTrackBody(
  @SerialName("track")
  val tracks: List<AlbumInfoTrackEntity>
) {
  @Serializable
  data class AlbumInfoTrackEntity(
    @SerialName("duration")
    val duration: String,

    @SerialName("url")
    val url: String,

    @SerialName("name")
    val name: String
  )
}

