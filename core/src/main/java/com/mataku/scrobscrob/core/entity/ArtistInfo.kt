package com.mataku.scrobscrob.core.entity

import kotlinx.collections.immutable.ImmutableList

data class ArtistInfo(
  val name: String,
  val images: ImmutableList<Image>,
  val tags: ImmutableList<Tag>,
  val stats: Stats,
  val url: String,
  val wiki: Wiki? = null
)
