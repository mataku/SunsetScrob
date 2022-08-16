package com.mataku.scrobscrob.account.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.account.ui.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.account.ui.state.rememberAccountState
import com.mataku.scrobscrob.account.ui.state.rememberThemeSelectorState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.PRIVACY_POLICY_DESTINATION
import com.mataku.scrobscrob.ui_common.style.ANIMATION_DURATION_MILLIS
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.TRANSITION_ANIMATION_DURATION_MILLIS
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.accountGraph(navController: NavController) {
  // TODO: first navigation from another screen should be fixed because unexpected diagonal effect
  composable(
    ACCOUNT_DESTINATION,
    enterTransition = {
      fadeIn(tween(ANIMATION_DURATION_MILLIS))
    },
    popEnterTransition = {
      fadeIn(tween(ANIMATION_DURATION_MILLIS))
    },
    exitTransition = {
      fadeOut(tween(ANIMATION_DURATION_MILLIS))
    },
    popExitTransition = {
      fadeOut(tween(ANIMATION_DURATION_MILLIS))
    }
  ) {
    val context = LocalContext.current
    AccountScreen(state = rememberAccountState(navController = navController, context = context))
  }

  composable(
    SCROBBLE_SETTING_DESTINATION,
    enterTransition = {
      slideIn(animationSpec = tween(TRANSITION_ANIMATION_DURATION_MILLIS)) { fullSize ->
        IntOffset(
          fullSize.width,
          0
        )
      }
    },
    exitTransition = {
      // Unnecessary as it is a dead end screen, but set up just in case
      slideOut(animationSpec = tween(TRANSITION_ANIMATION_DURATION_MILLIS)) { fullSize ->
        IntOffset(
          -fullSize.width,
          0
        )
      }
    },
    popEnterTransition = {
      // Unnecessary as it is a dead end screen, but set up just in case
      slideIn(animationSpec = tween(TRANSITION_ANIMATION_DURATION_MILLIS)) { fullSize ->
        IntOffset(
          -fullSize.width,
          0
        )
      }
    },
    popExitTransition = {
      slideOut(animationSpec = tween(TRANSITION_ANIMATION_DURATION_MILLIS)) { fullSize ->
        IntOffset(
          fullSize.width,
          0
        )
      }
    },

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
      state = rememberThemeSelectorState(
        navController = navController
      )
    )
  }
  composable(LICENSE_DESTINATION) {
    LicenseScreen()
  }
  composable(PRIVACY_POLICY_DESTINATION) {
    PrivacyPolicyScreen()
  }
}

fun NavController.navigateToAccount() {
  navigate(ACCOUNT_DESTINATION) {
    graph.startDestinationRoute?.let { screenRoute ->
      popUpTo(screenRoute) {
        saveState = true
      }
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
private const val SCROBBLE_SETTING_DESTINATION = "scrobble_setting"
private const val THEME_SELECTOR_DESTINATION = "theme_selector"
private const val LICENSE_DESTINATION = "license"

