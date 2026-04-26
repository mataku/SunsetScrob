package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.ui_common.component.SunsetText

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
      SunsetText.Title(
        text = "Home",
      )
    },
  ),
  DiscoverScreenStyle(
    route = "discover",
    topAppBarTitle = {
      SunsetText.Title(
        text = "Discover",
      )
    },
  ),
  AccountScreenStyle(
    route = "account",
    topAppBarTitle = {
      SunsetText.Title(
        text = "Account",
      )
    },
  ),
  ThemeSelectorScreenStyle(
    route = "theme_selector",
    topAppBarTitle = {
      SunsetText.Title(
        text = stringResource(
          id = com.mataku.scrobscrob.account.R.string.title_theme_selector
        ),
      )
    },
    navigationRequired = true,
  ),
  LicenseScreenStyle(
    route = "license",
    topAppBarTitle = {
      SunsetText.Title(
        text = stringResource(
          id = com.mataku.scrobscrob.account.R.string.item_license
        ),
      )
    },
    navigationRequired = true,
  ),
  PrivacyPolicyScreenStyle(
    route = "privacy_policy",
    topAppBarTitle = {
      SunsetText.Title(
        text = stringResource(
          id = com.mataku.scrobscrob.account.R.string.item_privacy_policy
        ),
      )
    },
    navigationRequired = true,
  ),
  ScrobbleSettingStyle(
    route = "scrobble_setting",
    topAppBarTitle = {
      SunsetText.Title(
        text = stringResource(id = R.string.label_scrobble_setting),
      )
    },
    navigationRequired = true
  ),
  WebViewStyle(
    route = "webview",
    topAppBarTitle = {
      SunsetText.Title(
        text = "",
      )
    },
    navigationRequired = true,
  );

  companion object {
    fun fromRoute(route: String?): ScreenStyle? {
      route ?: return null

      return entries.firstOrNull {
        it.route == route.split('?')[0]
      }
    }
  }
}
