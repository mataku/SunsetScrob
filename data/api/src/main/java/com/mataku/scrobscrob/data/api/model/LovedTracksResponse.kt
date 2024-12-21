package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LovedTracksResponse(
  @SerialName("lovedtracks")
  val lovedTracks: LovedTracks
)

@Serializable
data class LovedTracks(
  @SerialName("track")
  val tracks: List<LovedTrack>,

  @SerialName("@attr")
  val attr: PagingAttrBody
)

@Serializable
data class LovedTrack(
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
