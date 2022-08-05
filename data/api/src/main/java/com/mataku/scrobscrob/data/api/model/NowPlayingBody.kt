package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NowPlayingApiResponse(
  @SerialName("nowplaying")
  val nowPlaying: NowPlayingBody
)

@Serializable
data class NowPlayingBody(
  @SerialName("artist")
  val artist: NowPlayingArtistBody,
  @SerialName("album")
  val album: NowPlayingAlbumBody,
  @SerialName("track")
  val track: NowPlayingTrackBody
)

@Serializable
data class NowPlayingArtistBody(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val text: String
)

@Serializable
data class NowPlayingAlbumBody(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val text: String
)

@Serializable
data class NowPlayingTrackBody(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val text: String
)
