package com.mataku.scrobscrob.core.api.endpoint

import com.mataku.scrobscrob.core.entity.TopTags
import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TrackInfoEndpoint(
  override val path: String = "/2.0/?method=track.getInfo&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any?>
) : Endpoint

@Serializable
data class TrackInfoApiResponse(
  @SerialName("track")
  val trackInfo: TrackInfo
)

@Serializable
data class TrackInfo(
  @SerialName("duration")
  val duration: String? = null,
  @SerialName("album")
  val album: AlbumInfo? = null,
  @SerialName("listeners")
  val listeners: String,
  @SerialName("url")
  val url: String,
  @SerialName("toptags")
  val topTags: TopTags
)

@Serializable
data class AlbumInfo(
  @SerialName("artist")
  val artist: String,

  @SerialName("title")
  val title: String,

  @SerialName("image")
  val imageList: List<Image>
) {
  fun imageUrl(): String? = imageList.imageUrl()
}
