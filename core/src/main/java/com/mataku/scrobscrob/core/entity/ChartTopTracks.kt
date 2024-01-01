package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable

data class ChartTopTracks(
  val topTracks: List<ChartTrack>,
  val pagingAttr: PagingAttr
)

@Immutable
data class ChartTrack(
  val name: String,
  val playCount: String,
  val listeners: String,
  val url: String,
  val artist: ChartTrackArtist,
  val imageList: List<Image>
)

@Immutable
data class ChartTrackArtist(
  val name: String,
  val url: String
)
