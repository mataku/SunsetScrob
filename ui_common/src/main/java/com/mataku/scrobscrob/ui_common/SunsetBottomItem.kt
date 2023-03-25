package com.mataku.scrobscrob.ui_common

import androidx.annotation.DrawableRes

enum class SunsetBottomNavItem(
  @DrawableRes val iconDrawable: Int?,
  val title: String,
  val screenRoute: String
) {
  SCROBBLE(
    R.drawable.ic_last_fm_logo,
    "Scrobble",
    "scrobble"
  ),
  TOP_ALBUMS(
    R.drawable.ic_album_black_24px,
    "Top Albums",
    "top_albums"
  ),
  TOP_ARTISTS(
    R.drawable.ic_account_circle_black,
    "Top Artists",
    "top_artists"
  ),
  ACCOUNT(
    null,
    "Account",
    "account"
  );
}
