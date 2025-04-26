package com.mataku.scrobscrob.account.ui.navigation

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.ui_common.PRIVACY_POLICY_DESTINATION
import com.mataku.scrobscrob.ui_common.navigateToLogin
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy

fun NavGraphBuilder.accountGraph(navController: NavController) {
  navigation(
    route = ACCOUNT_NAVIGATION_ROUTE,
    startDestination = "${ACCOUNT_DESTINATION}?username={username}",
  ) {
    composable(
      ACCOUNT_DESTINATION,
      content = {
        val context = LocalContext.current
        AccountScreen(
          viewModel = hiltViewModel(),
          navigateToScrobbleSetting = navController::navigateToScrobbleSetting,
          navigateToThemeSelector = navController::navigateToThemeSelector,
          navigateToLicenseList = navController::navigateToLicense,
          navigateToLogin = navController::navigateToLogin,
          navigateToPrivacyPolicy = navController::navigateToPrivacyPolicy,
          showPermissionHelp = {
            Toast.makeText(
              context.applicationContext,
              R.string.label_notification_permission_help,
              Toast.LENGTH_LONG
            ).show()
          },
          modifier = Modifier
        )
      },
      enterTransition = {
        fadeIn(tween(250))
      },
      exitTransition = {
        fadeOut(tween(250))
      },
      popEnterTransition = {
        fadeIn(tween(250))
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(250))
      }
    )

    composable(
      SCROBBLE_SETTING_DESTINATION,
      content = {
        ScrobbleSettingScreen(
          viewModel = hiltViewModel(),
          modifier = Modifier
        )
      },
      enterTransition = {
        fadeIn(tween(250))
      },
      exitTransition = {
        fadeOut(tween(250))
      },
    )
    composable(
      THEME_SELECTOR_DESTINATION,
      content = {
        ThemeSelectorScreen(
          viewModel = hiltViewModel(),
          onBackPressed = navController::popBackStack,
          modifier = Modifier
        )
      },
      enterTransition = {
        fadeIn(tween(250))
      },
      exitTransition = {
        fadeOut(tween(250))
      },
    )
    composable(
      LICENSE_DESTINATION,
      content = {
        LicenseScreen(
          viewModel = hiltViewModel(),
          modifier = Modifier
        )
      },
      enterTransition = {
        fadeIn(tween(250))
      },
      exitTransition = {
        fadeOut(tween(250))
      }
    )
    composable(
      PRIVACY_POLICY_DESTINATION,
      content = {
        PrivacyPolicyScreen(
          modifier = Modifier
        )
      },
      enterTransition = {
        fadeIn(tween(250))
      },
      exitTransition = {
        fadeOut(tween(250))
      }
    )
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

