package com.mataku.scrobscrob.scrobble.ui.navigation

import android.net.Uri
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mataku.scrobscrob.scrobble.ui.screen.TrackScreen
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.scrobbleGraph(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
) {
  composable(
    "track_detail?trackName={trackName}&artistName={artistName}&imageUrl={imageUrl}&id={id}",
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
      navArgument("id") {
        type = NavType.StringType
      }
    ),
    content = {
      val arguments = it.arguments ?: return@composable

      val trackName = arguments.getString("trackName", "")
      val artistName = arguments.getString("artistName", "")
      val id = arguments.getString("id", "")

      with(sharedTransitionScope) {
        TrackScreen(
          trackName = trackName,
          artworkUrl = arguments.getString("imageUrl", ""),
          artistName = artistName,
          trackViewModel = hiltViewModel(),
          onBackPressed = navController::popBackStack,
          navigateToWebView = navController::navigateToWebView,
          id = id,
          animatedContentScope = this@composable,
        )
      }
    },
    enterTransition = {
      fadeIn(tween(300))
    },
    exitTransition = {
      fadeOut(tween(250))
    },
  )
}

fun NavController.navigateToTrackDetail(
  trackName: String,
  artistName: String,
  imageUrl: String,
  id: String,
) {
  val encodedTrackName = Uri.encode(trackName)
  val encodedArtistName = Uri.encode(artistName)
  val encodedId = Uri.encode(id)
  val destination =
    "track_detail?trackName=$encodedTrackName&artistName=$encodedArtistName&imageUrl=$imageUrl&id=$encodedId"
  navigate(destination)
}

private const val SCROBBLE_DESTINATION = "scrobble"
const val SCROBBLE_NAVIGATION_ROUTE = "scrobble_route"
