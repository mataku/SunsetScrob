package com.mataku.scrobscrob.album.ui.navigation

import androidx.navigation.NavController
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
    graph.startDestinationRoute?.let { screenRoute ->
      popUpTo(screenRoute) {
        saveState = true
      }
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val TOP_ALBUMS_DESTINATION = "top_albums"
