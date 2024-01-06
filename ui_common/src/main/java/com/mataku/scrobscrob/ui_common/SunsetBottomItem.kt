package com.mataku.scrobscrob.ui_common

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// NOTE: merge iconDrawable into ImageVector if possible
enum class SunsetBottomNavItem(
  @DrawableRes val iconDrawable: Int?,
  val title: String,
  val screenRoute: String,
  val icon: ImageVector?
) {
  SCROBBLE(
    R.drawable.ic_last_fm_logo,
    "Scrobble",
    "scrobble",
    null
  ),
  TOP_ALBUMS(
   null,
    "Album",
    "top_albums",
    Icons.Default.LibraryMusic
  ),
  TOP_ARTISTS(
    null,
    "Artist",
    "top_artists",
    Icons.Default.AccountCircle
  ),
  CHART(
    null,
    "Chart",
    "chart",
    Icons.Default.Public
  ),
  ACCOUNT(
    null,
    "Account",
    "account",
    Icons.Default.Settings
  );
}
