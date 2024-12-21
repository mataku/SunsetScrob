package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LovedTracksResponse(
  @SerialName("lovedtracks")
  val lovedTracks: LovedTracksBody
)

@Serializable
data class LovedTracksBody(
  @SerialName("track")
  val tracks: List<LovedTrackBody>,

  @SerialName("@attr")
  val attr: PagingAttrBody
)

@Serializable
data class LovedTrackBody(
  @SerialName("artist")
  val artist: TrackArtistBody,

  @SerialName("image")
  val images: List<ImageBody>,

  @SerialName("name")
  val name: String,

  @SerialName("url")
  val url: String,

  @SerialName("date")
  val date: RecentTrackDateBody? = null
)
