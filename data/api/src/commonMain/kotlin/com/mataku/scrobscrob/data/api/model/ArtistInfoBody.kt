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
  val tags: TagListBody? = null,

  @SerialName("stats")
  val stats: StatsBody,

  @SerialName("url")
  val url: String,

  @SerialName("bio")
  val wiki: WikiBody? = null
)

@Serializable
data class ArtistInfoApiResponse(
  @SerialName("artist")
  val artistInfo: ArtistInfoBody
)
