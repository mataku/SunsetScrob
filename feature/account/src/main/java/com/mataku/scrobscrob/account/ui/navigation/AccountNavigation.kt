package com.mataku.scrobscrob.account.ui.navigation

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.zacsweers.metrox.viewmodel.metroViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.screen.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.ui_common.PRIVACY_POLICY_DESTINATION
import com.mataku.scrobscrob.ui_common.navigateToLogin
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy
import com.mataku.scrobscrob.ui_common.navigation.PrivacyPolicyKey
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel

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
          viewModel = metroViewModel(),
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
          viewModel = metroViewModel(),
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
      THEME_SELECTOR_DESTINATION,
      content = {
        ThemeSelectorScreen(
          viewModel = metroViewModel(),
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
          viewModel = metroViewModel(),
          onBackPressed = navController::popBackStack,
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
          onBackPressed = navController::popBackStack,
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

fun SunsetNavBuilder.accountGraph() {
  destination<AccountKey> { key ->
    val context = LocalContext.current
    AccountScreen(
      viewModel = viewModelFor<AccountViewModel>(key),
      navigateToScrobbleSetting = { navigate(ScrobbleSettingKey) },
      navigateToThemeSelector = { navigate(ThemeSelectorKey) },
      navigateToLicenseList = { navigate(LicenseKey) },
      navigateToLogin = { /* 認証 gate 切替で処理、ここでは何もしない */ },
      navigateToPrivacyPolicy = { navigate(PrivacyPolicyKey) },
      showPermissionHelp = {
        Toast.makeText(
          context.applicationContext,
          R.string.label_notification_permission_help,
          Toast.LENGTH_LONG,
        ).show()
      },
      modifier = Modifier,
    )
  }
  destination<ScrobbleSettingKey> { key ->
    ScrobbleSettingScreen(
      viewModel = viewModelFor<ScrobbleSettingViewModel>(key),
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
  destination<ThemeSelectorKey> { key ->
    ThemeSelectorScreen(
      viewModel = viewModelFor<ThemeSelectorViewModel>(key),
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
  destination<LicenseKey> { key ->
    LicenseScreen(
      viewModel = viewModelFor<LicenseViewModel>(key),
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
  destination<PrivacyPolicyKey> {
    PrivacyPolicyScreen(
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
}

private const val ACCOUNT_DESTINATION = "account"
private const val ACCOUNT_NAVIGATION_ROUTE = "account_route"
private const val SCROBBLE_SETTING_DESTINATION = "scrobble_setting"
private const val THEME_SELECTOR_DESTINATION = "theme_selector"
private const val LICENSE_DESTINATION = "license"

