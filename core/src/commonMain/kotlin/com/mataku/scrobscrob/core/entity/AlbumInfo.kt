package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AlbumInfo(
  val albumName: String,
  val artistName: String,
  val url: String,
  val images: ImmutableList<Image>,
  val listeners: String,
  val playCount: String,
  val tags: ImmutableList<Tag>,
  val tracks: ImmutableList<AlbumInfoTrack>,
  val wiki: Wiki? = null
)

data class AlbumInfoTrack(
  val duration: String?,
  val url: String,
  val name: String
)
