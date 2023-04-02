package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ArtistInfo(
  val name: String,
  val imageList: ImmutableList<Image>,
  val topTags: ImmutableList<Tag>,
  val playCount: String,
  val url: String
)
