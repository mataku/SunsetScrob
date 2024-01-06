package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistInfoBody(
  @SerialName("name")
  val name: String,
  @SerialName("image")
  val imageList: List<ImageBody>? = null,

  @SerialName("tags")
  val topTags: TagsBody,

  @SerialName("stats")
  val stats: ArtistStatsBody,

  @SerialName("url")
  val url: String
)

@Serializable
data class ArtistInfoApiResponse(
  @SerialName("artist")
  val artistInfo: ArtistInfoBody
)

@Serializable
data class ArtistStatsBody(
  @SerialName("listeners")
  val listeners: String,

  @SerialName("playcount")
  val playCount: String
)
