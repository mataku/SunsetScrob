package com.mataku.scrobscrob.scrobble.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.scrobble.ui.screen.TrackScreen

fun NavGraphBuilder.scrobbleGraph(navController: NavController) {
  navigation(route = SCROBBLE_NAVIGATION_ROUTE, startDestination = "scrobble") {
    composable(
      SCROBBLE_DESTINATION,
    ) {
      val systemUiController = rememberSystemUiController()
      systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.background)
      ScrobbleScreen(
        viewModel = hiltViewModel(),
        navController = navController
      )
    }

    composable(
      "track_detail?trackName={trackName}&artistName={artistName}&imageUrl={imageUrl}",
      arguments = listOf(
        navArgument("trackName") {
          type = NavType.StringType
        },
        navArgument("artistName") {
          type = NavType.StringType
        },
        navArgument("imageUrl") {
          type = NavType.StringType
        },
      ),
      content = {
        val arguments = it.arguments ?: return@composable

        val trackName = arguments.getString("trackName", "")
        val artistName = arguments.getString("artistName", "")

        TrackScreen(
          trackName = trackName,
          artworkUrl = arguments.getString("imageUrl", ""),
          artistName = artistName,
          trackViewModel = hiltViewModel(),
        )
      }
    )
  }
}

fun NavController.navigateToTrackDetail(
  trackName: String,
  artistName: String,
  imageUrl: String,
) {
  val destination =
    "track_detail?trackName=$trackName&artistName=$artistName&imageUrl=$imageUrl"
  navigate(destination)
}

private const val SCROBBLE_DESTINATION = "scrobble"
const val SCROBBLE_NAVIGATION_ROUTE = "scrobble_route"
