package com.mataku.scrobscrob.scrobble.ui.navigation

import android.net.Uri
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mataku.scrobscrob.scrobble.ui.screen.TrackScreen
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.navigateToWebView
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

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
      // TODO: Phase 7 cleanup — old graph body disabled after TrackViewModel migrated to NavKey injection
      TODO("Phase 7 cleanup: use SunsetNavBuilder.scrobbleGraph() instead")
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

fun SunsetNavBuilder.scrobbleGraph() {
  destination<TrackDetailKey> { key ->
    TrackScreen(
      trackName = key.trackName,
      artworkUrl = key.imageUrl,
      artistName = key.artistName,
      trackViewModel = viewModelFor<TrackViewModel>(key),
      onBackPressed = ::popBackStack,
      navigateToWebView = { url -> navigate(WebViewKey(url)) },
      id = key.id,
      animatedContentScope = animatedContentScope,
    )
  }
}

private const val SCROBBLE_DESTINATION = "scrobble"
const val SCROBBLE_NAVIGATION_ROUTE = "scrobble_route"
