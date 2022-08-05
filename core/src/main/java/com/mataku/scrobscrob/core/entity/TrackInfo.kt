package com.mataku.scrobscrob.core.entity

data class TrackInfo(
  val duration: String? = null,
  val album: TrackAlbumInfo? = null,
  val listeners: String,
  val url: String,
  val topTags: List<Tag> = emptyList()
)
