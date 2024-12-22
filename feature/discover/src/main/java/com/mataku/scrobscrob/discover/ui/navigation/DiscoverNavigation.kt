package com.mataku.scrobscrob.discover.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.discover.ui.screen.DiscoverScreen
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.discoverGraph(navController: NavController) {
  navigation(route = DISCOVER_NAVIGATION_ROUTE, startDestination = DISCOVER_START_NAVIGATION) {
    composable(
      DISCOVER_START_NAVIGATION,
      content = {
        DiscoverScreen(
          viewModel = hiltViewModel(),
          navigateToWebView = navController::navigateToWebView,
          modifier = Modifier
            .padding(
              top = 24.dp
            )
        )
      },
      popEnterTransition = {
        EnterTransition.None
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(200))
      }
    )
  }
}

fun NavController.navigateToDiscover() {
  navigate(DISCOVER_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val DISCOVER_START_NAVIGATION = "discover"
private const val DISCOVER_NAVIGATION_ROUTE = "discover_route"
