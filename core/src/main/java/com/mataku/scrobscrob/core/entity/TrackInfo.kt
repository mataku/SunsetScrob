package com.mataku.scrobscrob.core.entity

data class TrackInfo(
  val duration: Long = 0L,
  val artist: TrackArtist,
  val album: TrackAlbumInfo? = null,
  val listeners: String,
  val url: String,
  val topTags: List<Tag> = emptyList(),
  val name: String
)

data class TrackArtist(
  val name: String,
  val url: String
)
