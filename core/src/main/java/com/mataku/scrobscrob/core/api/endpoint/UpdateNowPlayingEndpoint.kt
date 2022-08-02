package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class UpdateNowPlayingEndpoint(
  override val path: String = "/2.0/?method=track.updateNowPlaying&format=json",
  override val params: Map<String, Any>,
  override val requestType: HttpMethod = HttpMethod.Post
) : Endpoint

@Serializable
data class NowPlayingApiResponse(
  @SerialName("nowplaying")
  val nowPlaying: NowPlaying
)

@Serializable
data class NowPlaying(
  @SerialName("artist")
  val artist: NowPlayingArtist,
  @SerialName("album")
  val album: NowPlayingAlbum,
  @SerialName("track")
  val track: NowPlayingTrack
)

@Serializable
data class NowPlayingArtist(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val text: String
)

@Serializable
data class NowPlayingAlbum(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val text: String
)

@Serializable
data class NowPlayingTrack(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val text: String
)
