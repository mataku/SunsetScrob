package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TopArtists(
  val artists: ImmutableList<TopArtistInfo>,
  val pagingAttr: PagingAttr,
)
