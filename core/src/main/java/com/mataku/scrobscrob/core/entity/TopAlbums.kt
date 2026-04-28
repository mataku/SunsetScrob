package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TopAlbums(
  val albums: ImmutableList<TopAlbumInfo>,
  val pagingAttr: PagingAttr,
)
