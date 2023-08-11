package com.mataku.scrobscrob.album.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen

fun NavGraphBuilder.albumGraph(navController: NavController) {
  navigation(route = ALBUM_NAVIGATION_ROUTE, startDestination = TOP_ALBUMS_DESTINATION) {
    composable(
      TOP_ALBUMS_DESTINATION
    ) {
      TopAlbumsScreen(
        viewModel = hiltViewModel(),
        navController = navController
      )
    }
  }
}

fun NavController.navigateToTopAlbums() {
  navigate(ALBUM_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val TOP_ALBUMS_DESTINATION = "top_albums"
private const val ALBUM_NAVIGATION_ROUTE = "album_route"
