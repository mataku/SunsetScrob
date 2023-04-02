package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class RecentTrack(
  val artistName: String,
  val images: ImmutableList<Image>,
  val albumName: String,
  val name: String,
  val url: String,
  val date: String? = null
)
