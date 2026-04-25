package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ChartTopArtists(
  val topArtists: ImmutableList<ChartArtist>,
  val pagingAttr: PagingAttr
)

@Immutable
data class ChartArtist(
  val name: String,
  val playCount: String,
  val listeners: String,
  val url: String,
  val imageList: List<Image>,
  val imageUrl: String? = null
)
