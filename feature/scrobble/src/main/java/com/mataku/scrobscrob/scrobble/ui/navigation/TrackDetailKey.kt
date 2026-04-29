package com.mataku.scrobscrob.scrobble.ui.navigation

import androidx.compose.runtime.Immutable
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class TrackDetailKey(
  val trackName: String,
  val artistName: String,
  val imageUrl: String,
  val id: String,
) : SunsetNavKey
