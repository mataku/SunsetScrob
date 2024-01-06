package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChartTopTracksResponse(
  @SerialName("tracks")
  val chartTopTracksBody: ChartTopTracksBody
)

@Serializable
data class ChartTopTracksBody(
  @SerialName("track")
  val topTracks: List<ChartTrack>,

  @SerialName("@attr")
  val pagingAttrBody: PagingAttrBody
)

@Serializable
data class ChartTrack(
  @SerialName("name")
  val name: String,

  @SerialName("playcount")
  val playCount: String,

  @SerialName("listeners")
  val listeners: String,

  @SerialName("url")
  val url: String,

  @SerialName("artist")
  val artist: ChartTrackArtist,

  @SerialName("image")
  val imageList: List<ImageBody>,

  @SerialName("mbid")
  val mbid: String = ""
)

@Serializable
data class ChartTrackArtist(
  @SerialName("name")
  val name: String,

  @SerialName("url")
  val url: String,
)
