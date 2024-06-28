package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TopArtistInfo(
  val name: String,
  val imageList: ImmutableList<Image>,
  val topTags: ImmutableList<Tag>,
  val playCount: String,
  val url: String,
  var imageUrl: String? = null
)
