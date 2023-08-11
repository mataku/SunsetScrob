package com.mataku.scrobscrob.account.ui.navigation

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.PRIVACY_POLICY_DESTINATION
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor

fun NavGraphBuilder.accountGraph(navController: NavController) {
  navigation(route = ACCOUNT_NAVIGATION_ROUTE, startDestination = ACCOUNT_DESTINATION) {
    composable(
      ACCOUNT_DESTINATION,
    ) {
      val context = LocalContext.current
      AccountScreen(
        viewModel = hiltViewModel(),
        navController = navController,
        showPermissionHelp = {
          Toast.makeText(
            context.applicationContext,
            R.string.label_notification_permission_help,
            Toast.LENGTH_LONG
          ).show()
        }
      )
    }

    composable(
      SCROBBLE_SETTING_DESTINATION,
    ) {
      val systemUiController = rememberSystemUiController()
      systemUiController.setNavigationBarColor(
        color = if (LocalAppTheme.current == AppTheme.SUNSET) {
          Colors.SunsetBlue
        } else {
          LocalAppTheme.current.backgroundColor()
        }
      )
      ScrobbleSettingScreen()
    }
    composable(THEME_SELECTOR_DESTINATION) {
      ThemeSelectorScreen(
        viewModel = hiltViewModel(),
        navController = navController
      )
    }
    composable(LICENSE_DESTINATION) {
      LicenseScreen()
    }
    composable(PRIVACY_POLICY_DESTINATION) {
      PrivacyPolicyScreen()
    }
  }
}

fun NavController.navigateToAccount() {
  navigate(ACCOUNT_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

fun NavController.navigateToScrobbleSetting() {
  navigate(SCROBBLE_SETTING_DESTINATION)
}

fun NavController.navigateToThemeSelector() {
  navigate(THEME_SELECTOR_DESTINATION)
}

fun NavController.navigateToLicense() {
  navigate(LICENSE_DESTINATION)
}

private const val ACCOUNT_DESTINATION = "account"
private const val ACCOUNT_NAVIGATION_ROUTE = "account_route"
private const val SCROBBLE_SETTING_DESTINATION = "scrobble_setting"
private const val THEME_SELECTOR_DESTINATION = "theme_selector"
private const val LICENSE_DESTINATION = "license"

