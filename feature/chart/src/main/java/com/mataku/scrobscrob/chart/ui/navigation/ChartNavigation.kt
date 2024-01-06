package com.mataku.scrobscrob.chart.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.chart.ui.screen.ChartScreen

fun NavGraphBuilder.chartGraph(navController: NavController) {
  navigation(route = CHART_NAVIGATION_ROUTE, startDestination = CHART_START_NAVIGATION) {
    composable(
      CHART_START_NAVIGATION
    ) {
      ChartScreen(
        navController = navController
      )
    }
  }
}

fun NavController.navigateToChart() {
  navigate(CHART_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val CHART_START_NAVIGATION = "chart"
private const val CHART_NAVIGATION_ROUTE = "chart_route"
