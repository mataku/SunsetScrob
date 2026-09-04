package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class LovedTrack(
  val artist: String,
  val images: ImmutableList<Image>,
  val name: String,
  val url: String,
  val date: String? = null,
  val imageUrl: String? = null
)
