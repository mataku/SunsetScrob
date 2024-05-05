package com.mataku.scrobscrob.home.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.home.ui.HomeScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
  navigation(route = HOME_NAVIGATION_ROUTE, startDestination = HOME_DESTINATION) {
    composable(
      HOME_DESTINATION,
    ) {
      HomeScreen(
        navController = navController
      )
    }
  }
}

fun NavController.navigateToHome() {
  navigate(HOME_NAVIGATION_ROUTE)
}

private const val HOME_DESTINATION = "home"
const val HOME_NAVIGATION_ROUTE = "home_route"
