package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("album")
data class AlbumBody(
  @SerialName("name")
  val name: String,
  @SerialName("url")
  val url: String,
  @SerialName("artist")
  val artist: AlbumArtistBody,
  @SerialName("image")
  val imageList: List<ImageBody>? = null,
  @SerialName("playcount")
  val playcount: String
)

@Serializable
data class UserTopAlbumsApiResponse(
  @SerialName("topalbums")
  val topAlbums: AlbumsBody
)

@Serializable
data class AlbumsBody(
  @SerialName("album")
  val albums: List<AlbumBody>
)

@Serializable
@SerialName("artist")
data class AlbumArtistBody(
  @SerialName("name")
  val name: String
)
