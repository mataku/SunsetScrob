package com.mataku.scrobscrob.scrobble.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.scrobble.ui.screen.TrackScreen
import com.mataku.scrobscrob.scrobble.ui.state.rememberScrobbleScreenState
import com.mataku.scrobscrob.scrobble.ui.state.rememberTrackScreenState
import com.mataku.scrobscrob.ui_common.style.ANIMATION_DURATION_MILLIS

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.scrobbleGraph(navController: NavController) {
  composable(
    SCROBBLE_DESTINATION,
    enterTransition = {
      fadeIn(tween(ANIMATION_DURATION_MILLIS))
    },
    exitTransition = {
      fadeOut(tween(ANIMATION_DURATION_MILLIS))
    },
    popEnterTransition = {
      fadeIn(tween(ANIMATION_DURATION_MILLIS))
    },
    popExitTransition = {
      fadeOut(tween(ANIMATION_DURATION_MILLIS))
    }
  ) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(MaterialTheme.colors.primary)
    ScrobbleScreen(
      state = rememberScrobbleScreenState(navController = navController)
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
      val artistName = arguments.getString("artistName", "")

      TrackScreen(
        trackName = trackName,
        artworkUrl = arguments.getString("imageUrl", ""),
        topLeftCoordinate = Pair(x, y),
        screenState = rememberTrackScreenState(
          navController = navController,
          trackName = trackName,
          artistName = artistName
        )
      )
    }
  )
}

fun NavController.navigateToScrobble() {
  navigate(SCROBBLE_DESTINATION) {
    graph.startDestinationRoute?.let { screenRoute ->
      popUpTo(screenRoute) {
        saveState = true
      }
    }
    launchSingleTop = true
    restoreState = true
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
