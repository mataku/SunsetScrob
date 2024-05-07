package com.mataku.scrobscrob.home.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.album.ui.navigation.albumGraph
import com.mataku.scrobscrob.artist.ui.navigation.artistGraph
import com.mataku.scrobscrob.home.ui.HomeScreen
import com.mataku.scrobscrob.scrobble.ui.navigation.scrobbleGraph

fun NavGraphBuilder.homeGraph(navController: NavController) {
  navigation(route = HOME_NAVIGATION_ROUTE, startDestination = HOME_DESTINATION) {
    composable(
      HOME_DESTINATION,
    ) {
      HomeScreen(
        navController = navController
      )
    }

    albumGraph(navController)
    artistGraph(navController)
    scrobbleGraph(navController)
  }
}

fun NavController.navigateToHome() {
  navigate(HOME_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val HOME_DESTINATION = "home"
const val HOME_NAVIGATION_ROUTE = "home_route"
