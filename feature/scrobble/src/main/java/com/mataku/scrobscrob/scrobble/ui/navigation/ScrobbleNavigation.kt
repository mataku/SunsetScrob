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
      "track_detail?trackName={trackName}&artistName={artistName}&imageUrl={imageUrl}&upperLeftCoordinatorX={x}&upperLeftCoordinatorY={y}",
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
        navArgument("x") {
          type = NavType.IntType
        },
        navArgument("y") {
          type = NavType.IntType
        }
      ),
      content = {
        val arguments = it.arguments ?: return@composable
        val x = arguments.getInt("x", 0)
        val y = arguments.getInt("y", 0)

        val trackName = arguments.getString("trackName", "")

        TrackScreen(
          trackName = trackName,
          artworkUrl = arguments.getString("imageUrl", ""),
          topLeftCoordinate = Pair(x, y),
          trackViewModel = hiltViewModel(),
          navController = navController
        )
      }
    )
  }
}

fun NavController.navigateToTrackDetail(
  trackName: String,
  artistName: String,
  imageUrl: String,
  x: Int,
  y: Int
) {
  val destination =
    "track_detail?trackName=$trackName&artistName=$artistName&imageUrl=$imageUrl&upperLeftCoordinatorX=$x&upperLeftCoordinatorY=$y"
  navigate(destination)
}

private const val SCROBBLE_DESTINATION = "scrobble"
const val SCROBBLE_NAVIGATION_ROUTE = "scrobble_route"
