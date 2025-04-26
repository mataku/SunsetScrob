package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle

enum class ScreenStyle(
  val route: String,
  val topAppBarTitle: @Composable () -> Unit,
  val navigationRequired: Boolean = false
) {
  MainScreenStyle(
    route = "main",
    topAppBarTitle = {},
  ),
  HomeScreenStyle(
    route = "home",
    topAppBarTitle = {
      Text(
        text = "Home",
        style = SunsetTextStyle.title,
      )
    },
  ),
  DiscoverScreenStyle(
    route = "discover",
    topAppBarTitle = {
      Text(
        text = "Discover",
        style = SunsetTextStyle.title,
      )
    },
  ),
  AccountScreenStyle(
    route = "account",
    topAppBarTitle = {
      Text(
        text = "Account",
        style = SunsetTextStyle.title,
      )
    },
  ),
  ThemeSelectorScreenStyle(
    route = "theme_selector",
    topAppBarTitle = {
      Text(
        text = stringResource(
          id = com.mataku.scrobscrob.account.R.string.title_theme_selector
        ),
        style = SunsetTextStyle.title,
      )
    },
    navigationRequired = true,
  ),
  LicenseScreenStyle(
    route = "license",
    topAppBarTitle = {
      Text(
        text = stringResource(
          id = com.mataku.scrobscrob.account.R.string.item_license
        ),
        style = SunsetTextStyle.title,
      )
    },
    navigationRequired = true,
  ),
  PrivacyPolicyScreenStyle(
    route = "privacy_policy",
    topAppBarTitle = {
      Text(
        text = stringResource(
          id = com.mataku.scrobscrob.account.R.string.item_privacy_policy
        ),
        style = SunsetTextStyle.title,
      )
    },
    navigationRequired = true,
  ),
  ScrobbleSettingStyle(
    route = "scrobble_setting",
    topAppBarTitle = {
      Text(
        stringResource(id = R.string.label_scrobble_setting),
        style = SunsetTextStyle.title,
      )
    },
    navigationRequired = true
  );

  companion object {
    fun fromRoute(route: String?): ScreenStyle? {
      route ?: return null
      return entries.firstOrNull { it.route == route }
    }
  }
}
