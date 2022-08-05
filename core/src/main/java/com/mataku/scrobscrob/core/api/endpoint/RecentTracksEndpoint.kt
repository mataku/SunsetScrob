package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RecentTracksEndpoint(
  override val path: String = "/2.0/?method=user.getrecenttracks&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any>
) : Endpoint

@Serializable
data class RecentTrackApiResponse(
  @SerialName("recenttracks")
  val recentTracks: RecentTracks
)

@Serializable
data class RecentTracks(
  @SerialName("track")
  val tracks: List<RecentTrack>,

  @SerialName("@attr")
  val attr: RecentTrackAttr
)

@Serializable
data class RecentTrack(
  @SerialName("artist")
  val artist: RecentTrackArtist,

  @SerialName("image")
  val images: List<Image>,

  @SerialName("album")
  val album: RecentTrackAlbum,

  @SerialName("name")
  val name: String,

  @SerialName("url")
  val url: String,

  @SerialName("date")
  val date: RecentTrackDate? = null
) {
  fun imageUrl(): String? =
    images.imageUrl()

  fun isNowPlayingTrack(): Boolean = date == null
}

@Serializable
data class RecentTrackArtist(
  @SerialName("#text")
  val name: String
)

@Serializable
data class RecentTrackAlbum(
  @SerialName("#text")
  val name: String
)

@Serializable
data class RecentTrackDate(
  @SerialName("#text")
  val date: String
)

@Serializable
data class RecentTrackImage(
  @SerialName("size")
  val size: String,

  @SerialName("#text")
  val url: String
)

@Serializable
data class RecentTrackAttr(
  @SerialName("user")
  val user: String,

  @SerialName("totalPages")
  val totalPages: String
)

