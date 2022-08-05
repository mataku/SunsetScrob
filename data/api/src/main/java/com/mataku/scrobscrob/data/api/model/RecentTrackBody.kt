package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentTracksApiResponse(
  @SerialName("recenttracks")
  val recentTracks: RecentTracksBody
)

@Serializable
data class RecentTracksBody(
  @SerialName("track")
  val tracks: List<RecentTrack>,

  @SerialName("@attr")
  val attr: RecentTrackAttrBody
)

@Serializable
data class RecentTrack(
  @SerialName("artist")
  val artist: RecentTrackArtistBody,

  @SerialName("image")
  val images: List<ImageBody>,

  @SerialName("album")
  val album: RecentTrackAlbumBody,

  @SerialName("name")
  val name: String,

  @SerialName("url")
  val url: String,

  @SerialName("date")
  val date: RecentTrackDateBody? = null
) {
  fun isNowPlayingTrack(): Boolean = date == null
}

@Serializable
data class RecentTrackArtistBody(
  @SerialName("#text")
  val name: String
)

@Serializable
data class RecentTrackAlbumBody(
  @SerialName("#text")
  val name: String
)

@Serializable
data class RecentTrackDateBody(
  @SerialName("#text")
  val date: String
)

@Serializable
data class RecentTrackImageBody(
  @SerialName("size")
  val size: String,

  @SerialName("#text")
  val url: String
)

@Serializable
data class RecentTrackAttrBody(
  @SerialName("user")
  val user: String,

  @SerialName("totalPages")
  val totalPages: String
)

