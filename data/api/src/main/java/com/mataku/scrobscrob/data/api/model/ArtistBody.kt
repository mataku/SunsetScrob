package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("artist")
data class ArtistBody(
  @SerialName("name")
  val name: String,
  @SerialName("url")
  val url: String,
  @SerialName("playcount")
  val playcount: String,
  @SerialName("image")
  val imageList: List<ImageBody>?
)

@Serializable
data class TopArtistsBody(
  @SerialName("artist")
  val artists: List<ArtistBody>
)

@Serializable
data class UserTopArtistsApiResponse(
  @SerialName("topartists")
  val topArtists: TopArtistsBody
)


