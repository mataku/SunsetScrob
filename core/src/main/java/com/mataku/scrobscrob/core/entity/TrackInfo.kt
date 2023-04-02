package com.mataku.scrobscrob.core.entity

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TrackInfo(
  val duration: Long = 0L,
  val artist: TrackArtist,
  val album: TrackAlbumInfo? = null,
  val listeners: String,
  val url: String,
  val topTags: ImmutableList<Tag> = persistentListOf(),
  val name: String
)

data class TrackArtist(
  val name: String,
  val url: String
)
