package com.mataku.scrobscrob.home.ui.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mataku.scrobscrob.album.ui.navigation.albumGraph
import com.mataku.scrobscrob.album.ui.navigation.navigateToAlbumInfo
import com.mataku.scrobscrob.artist.ui.navigation.artistGraph
import com.mataku.scrobscrob.artist.ui.navigation.navigateToArtistInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.home.ui.HomeScreen
import com.mataku.scrobscrob.scrobble.ui.navigation.navigateToTrackDetail
import com.mataku.scrobscrob.scrobble.ui.navigation.scrobbleGraph
import com.mataku.scrobscrob.ui_common.navigateToLogin

fun NavGraphBuilder.homeGraph(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
) {
  navigation(route = HOME_NAVIGATION_ROUTE, startDestination = HOME_DESTINATION) {
    composable(
      HOME_DESTINATION,
      content = {
        HomeScreen(
          viewModel = hiltViewModel(key = "home"),
          sharedTransitionScope = sharedTransitionScope,
          animatedContentScope = this@composable,
          navigateToTrackDetail = { track, id ->
            navController.navigateToTrackDetail(
              trackName = track.name,
              artistName = track.artistName,
              imageUrl = track.images.imageUrl() ?: "",
              id = id
            )
          },
          navigateToArtistDetail = { artist, id ->
            navController.navigateToArtistInfo(
              artistName = artist.name,
              artworkUrl = (artist.imageUrl ?: artist.imageList.imageUrl()) ?: "",
              contentId = id
            )
          },
          navigateToAlbumDetail = { album, id ->
            navController.navigateToAlbumInfo(
              albumName = album.title,
              artistName = album.artist,
              artworkUrl = album.imageList.imageUrl() ?: "",
              contentId = id
            )
          },
          navigateToLogin = {
            navController.navigateToLogin()
          },
          modifier = Modifier
            .padding(
              top = 24.dp
            )
        )
      },
      enterTransition = {
        fadeIn(tween(300))
      },
      exitTransition = {
        fadeOut(tween(250))
      },
    )

    albumGraph(
      navController = navController,
      sharedTransitionScope = sharedTransitionScope
    )
    artistGraph(
      navController = navController,
      sharedTransitionScope = sharedTransitionScope,
    )
    scrobbleGraph(
      navController = navController,
      sharedTransitionScope = sharedTransitionScope,
    )
  }
}

fun NavController.navigateToHome() {
  navigate(HOME_DESTINATION) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val HOME_DESTINATION = "home"
const val HOME_NAVIGATION_ROUTE = "home_route"
