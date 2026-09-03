package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChartTopArtistsResponse(
  @SerialName("artists")
  val chartTopArtistsBody: ChartTopArtistsBody
)

@Serializable
data class ChartTopArtistsBody(
  @SerialName("artist")
  val topArtists: List<ChartArtist>,

  @SerialName("@attr")
  val pagingAttrBody: PagingAttrBody
)

@Serializable
data class ChartArtist(
  @SerialName("name")
  val name: String,

  @SerialName("playcount")
  val playCount: String,

  @SerialName("listeners")
  val listeners: String,

  @SerialName("url")
  val url: String,

  @SerialName("image")
  val imageList: List<ImageBody>
)
