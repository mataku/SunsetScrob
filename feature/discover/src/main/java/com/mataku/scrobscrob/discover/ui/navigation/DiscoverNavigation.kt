package com.mataku.scrobscrob.discover.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.discover.ui.screen.DiscoverScreen

fun NavGraphBuilder.discoverGraph(navController: NavController) {
  navigation(route = DISCOVER_NAVIGATION_ROUTE, startDestination = DISCOVER_START_NAVIGATION) {
    composable(
      DISCOVER_START_NAVIGATION
    ) {
      DiscoverScreen(
        viewModel = hiltViewModel(),
        navController = navController
      )
    }
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
