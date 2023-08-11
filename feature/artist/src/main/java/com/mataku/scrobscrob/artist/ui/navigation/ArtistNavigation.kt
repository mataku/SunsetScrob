package com.mataku.scrobscrob.artist.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen

fun NavGraphBuilder.artistGraph(navController: NavController) {
  navigation(route = ARTIST_NAVIGATION_ROUTE, startDestination = TOP_ARTISTS_DESTINATION) {
    composable(TOP_ARTISTS_DESTINATION) {
      TopArtistsScreen(
        viewModel = hiltViewModel(),
        navController = navController
      )
    }
  }
}

fun NavController.navigateToTopArtists() {
  navigate(ARTIST_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val TOP_ARTISTS_DESTINATION = "top_artists"
private const val ARTIST_NAVIGATION_ROUTE = "artist_route"
