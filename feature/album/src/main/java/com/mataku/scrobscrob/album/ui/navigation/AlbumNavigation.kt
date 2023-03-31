package com.mataku.scrobscrob.album.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.album.ui.state.rememberTopAlbumsScreenState

fun NavGraphBuilder.albumGraph(navController: NavController) {
  composable(
    TOP_ALBUMS_DESTINATION
  ) {
    TopAlbumsScreen(
      state = rememberTopAlbumsScreenState(navController = navController)
    )
  }
}

fun NavController.navigateToTopAlbums() {
  navigate(TOP_ALBUMS_DESTINATION) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val TOP_ALBUMS_DESTINATION = "top_albums"
