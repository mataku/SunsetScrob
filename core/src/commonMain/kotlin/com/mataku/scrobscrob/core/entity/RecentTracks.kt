package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class RecentTracks(
  val tracks: ImmutableList<RecentTrack>,
  val pagingAttr: PagingAttr,
)
