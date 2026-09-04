package com.mataku.scrobscrob.album.ui.navigation

import androidx.compose.runtime.Immutable
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class AlbumKey(
  val albumName: String,
  val artistName: String,
  val artworkUrl: String,
  val contentId: String,
) : SunsetNavKey
