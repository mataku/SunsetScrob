package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TopAlbumInfo(
  val artist: String,
  val title: String,
  val imageList: ImmutableList<Image>,
  val playCount: String,
  val url: String
)

data class TrackAlbumInfo(
  val artist: String,
  val title: String,
  val imageList: ImmutableList<Image>
  // playCount is missing in track.getInfo
  // https://www.last.fm/api/show/track.getInfo
)
