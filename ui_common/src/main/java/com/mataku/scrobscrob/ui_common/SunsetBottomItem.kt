package com.mataku.scrobscrob.ui_common

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
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
  HOME(
    R.drawable.ic_last_fm_logo,
    "Home",
    "home",
    null
  ),
  DISCOVER(
    null,
    "Discover",
    "discover",
    Icons.Default.Public
  ),
  ACCOUNT(
    null,
    "Account",
    "account",
    Icons.Default.Settings
  );

  companion object {
    fun currentItem(route: String?): SunsetBottomNavItem? {
      route ?: return null

      val entry = entries.find { it.screenRoute == route }
      if (entry != null) {
        return entry
      }

      if (route in listOf("scrobble_setting", "theme_selector", "license")) {
        return ACCOUNT
      }
      return HOME
    }
  }
}
