package com.mataku.scrobscrob.album.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.album.ui.state.rememberTopAlbumsScreenState

@OptIn(ExperimentalAnimationApi::class)
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
