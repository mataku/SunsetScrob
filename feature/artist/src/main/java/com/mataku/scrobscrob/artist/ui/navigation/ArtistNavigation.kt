package com.mataku.scrobscrob.artist.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.artist.ui.state.rememberTopArtistsScreenState

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.artistGraph(navController: NavController) {
  composable(TOP_ARTISTS_DESTINATION) {
    TopArtistsScreen(
      state = rememberTopArtistsScreenState(navController = navController)
    )
  }

}

fun NavController.navigateToTopArtists() {
  navigate(TOP_ARTISTS_DESTINATION) {
    graph.startDestinationRoute?.let { screenRoute ->
      popUpTo(screenRoute) {
        saveState = true
      }
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val TOP_ARTISTS_DESTINATION = "top_artists"
