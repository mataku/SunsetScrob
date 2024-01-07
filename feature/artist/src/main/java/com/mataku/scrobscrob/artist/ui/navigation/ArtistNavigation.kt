package com.mataku.scrobscrob.artist.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mataku.scrobscrob.artist.ui.screen.ArtistScreen
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.artistGraph(navController: NavController) {
  navigation(route = ARTIST_NAVIGATION_ROUTE, startDestination = TOP_ARTISTS_DESTINATION) {
    composable(TOP_ARTISTS_DESTINATION) {
      TopArtistsScreen(
        viewModel = hiltViewModel(),
        onArtistTap = {
          navController.navigateToArtistInfo(
            artistName = it.name,
            artworkUrl = it.imageList.imageUrl() ?: "",
          )
        }
      )
    }

    composable(
      "${ARTIST_INFO_DESTINATION}?${ARTIST_NAME_ARGUMENT}={artistName}&${ARTWORK_URL_ARGUMENT}={artworkUrl}",
      arguments = listOf(
        navArgument(ARTIST_NAME_ARGUMENT) {
          type = NavType.StringType
        },
        navArgument(ARTWORK_URL_ARGUMENT) {
          type = NavType.StringType
        }
      )
    ) {
      ArtistScreen(
        viewModel = hiltViewModel(),
        onArtistLoadMoreTap = navController::navigateToWebView
      )
    }
  }
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
  artworkUrl: String
) {
  val destination =
    "${ARTIST_INFO_DESTINATION}?${ARTIST_NAME_ARGUMENT}=${artistName}&${ARTWORK_URL_ARGUMENT}=${artworkUrl}"
  navigate(destination)
}

private const val ARTIST_NAME_ARGUMENT = "artistName"
private const val ARTWORK_URL_ARGUMENT = "artworkUrl"

private const val ARTIST_INFO_DESTINATION = "top_artists"
private const val TOP_ARTISTS_DESTINATION = "top_artists"
private const val ARTIST_NAVIGATION_ROUTE = "artist_route"
