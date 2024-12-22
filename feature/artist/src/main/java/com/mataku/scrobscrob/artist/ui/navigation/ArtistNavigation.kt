@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mataku.scrobscrob.artist.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mataku.scrobscrob.artist.ui.screen.ArtistScreen
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.artistGraph(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope
) {
  composable(
    "${ARTIST_INFO_DESTINATION}?${ARTIST_NAME_ARGUMENT}={artistName}&${ARTWORK_URL_ARGUMENT}={artworkUrl}&${ARTWORK_ID_ARGUMENT}={id}",
    arguments = listOf(
      navArgument(ARTIST_NAME_ARGUMENT) {
        type = NavType.StringType
      },
      navArgument(ARTWORK_URL_ARGUMENT) {
        type = NavType.StringType
      },
      navArgument(ARTWORK_ID_ARGUMENT) {
        type = NavType.StringType
      },
    ),
    content = {
      val contentId = it.arguments?.getString(ARTWORK_ID_ARGUMENT, "") ?: ""
      with(sharedTransitionScope) {
        ArtistScreen(
          id = contentId,
          animatedContentScope = this@composable,
          viewModel = hiltViewModel(),
          onArtistLoadMoreTap = navController::navigateToWebView,
          onBackPressed = navController::popBackStack
        )
      }
    },
    enterTransition = {
      fadeIn(tween(300))
    },
    exitTransition = {
      fadeOut(tween(300))
    }
  )
}

fun NavController.navigateToTopArtists() {
  navigate(ARTIST_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

fun NavController.navigateToArtistInfo(
  artistName: String,
  artworkUrl: String,
  contentId: String,
) {
  val destination =
    "${ARTIST_INFO_DESTINATION}?${ARTIST_NAME_ARGUMENT}=${artistName}&${ARTWORK_URL_ARGUMENT}=${artworkUrl}&${ARTWORK_ID_ARGUMENT}=${contentId}"
  navigate(destination)
}

private const val ARTIST_NAME_ARGUMENT = "artistName"
private const val ARTWORK_URL_ARGUMENT = "artworkUrl"
private const val ARTWORK_ID_ARGUMENT = "id"

private const val ARTIST_INFO_DESTINATION = "top_artists"
private const val TOP_ARTISTS_DESTINATION = "top_artists"
private const val ARTIST_NAVIGATION_ROUTE = "artist_route"
