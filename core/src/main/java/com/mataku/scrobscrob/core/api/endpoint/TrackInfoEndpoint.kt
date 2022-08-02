package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TrackInfoEndpoint(
  override val path: String = "/2.0/?method=track.getInfo&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any>
) : Endpoint

@Serializable
data class TrackInfoApiResponse(
  @SerialName("track")
  val trackInfo: TrackInfo
)

@Serializable
data class TrackInfo(
  @SerialName("duration")
  val duration: String?,
  @SerialName("album")
  val album: AlbumInfo?
)

@Serializable
data class AlbumInfo(
  @SerialName("image")
  val imageList: List<Image>
)
