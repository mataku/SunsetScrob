package com.mataku.scrobscrob.account.ui.navigation

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.ui_common.PRIVACY_POLICY_DESTINATION

fun NavGraphBuilder.accountGraph(navController: NavController, username: String) {
  val usernameArgs = "username"
  navigation(
    route = ACCOUNT_NAVIGATION_ROUTE,
    startDestination = "${ACCOUNT_DESTINATION}?username={username}",
  ) {
    composable(
      "${ACCOUNT_DESTINATION}?username={username}",
      arguments = listOf(
        navArgument(usernameArgs) {
          type = NavType.StringType
          defaultValue = username
        }
      )
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
      ScrobbleSettingScreen(
        viewModel = hiltViewModel()
      )
    }
    composable(THEME_SELECTOR_DESTINATION) {
      ThemeSelectorScreen(
        viewModel = hiltViewModel(),
        navController = navController
      )
    }
    composable(LICENSE_DESTINATION) {
      LicenseScreen(
        viewModel = hiltViewModel(),
        onBackPressed = navController::popBackStack
      )
    }
    composable(PRIVACY_POLICY_DESTINATION) {
      PrivacyPolicyScreen(
        onBackPressed = navController::popBackStack
      )
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

