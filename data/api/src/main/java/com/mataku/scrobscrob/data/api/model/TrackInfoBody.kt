package com.mataku.scrobscrob.data.api.model

import com.mataku.scrobscrob.data.api.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class TrackInfoBody(
  // milliseconds
  @SerialName("duration")
  val duration: Long = 0L,
  @SerialName("album")
  val album: TrackAlbumInfoBody? = null,
  @SerialName("listeners")
  val listeners: String,
  @SerialName("playcount")
  val playCount: String,
  @SerialName("url")
  val url: String,
  @SerialName("toptags")
  val topTags: TagsBody,
  @SerialName("artist")
  val artist: TrackArtistBody,
  @SerialName("name")
  val name: String,
  @SerialName("wiki")
  val wiki: TrackArtistWikiBody? = null
)

@Serializable
data class TrackAlbumInfoBody(
  @SerialName("artist")
  val artist: String,

  @SerialName("title")
  val title: String,

  @SerialName("image")
  val imageList: List<ImageBody>
)

@Serializable
data class TrackArtistBody(
  val name: String,
  val url: String
)

@Serializable
data class TrackArtistWikiBody(
  @Serializable(DateSerializer::class)
  val published: Date,

  @SerialName("summary")
  val summary: String? = null,

  @SerialName("content")
  val content: String? = null
)

