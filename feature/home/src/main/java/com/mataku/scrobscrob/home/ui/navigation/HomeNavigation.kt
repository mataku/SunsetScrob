package com.mataku.scrobscrob.home.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeGraph(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
) {
  navigation(route = HOME_NAVIGATION_ROUTE, startDestination = HOME_DESTINATION) {
    composable(
      HOME_DESTINATION,
    ) {
      HomeScreen(
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
        navigateToArtistDetail = { artist ->
          navController.navigateToArtistInfo(
            artistName = artist.name,
            artworkUrl = (artist.imageUrl ?: artist.imageList.imageUrl()) ?: ""
          )
        },
        navigateToAlbumDetail = { album ->
          navController.navigateToAlbumInfo(
            albumName = album.title,
            artistName = album.artist,
            artworkUrl = album.imageList.imageUrl() ?: ""
          )
        },
        modifier = Modifier
          .padding(
            top = 24.dp
          )
      )
    }

    albumGraph(navController)
    artistGraph(navController)
    scrobbleGraph(
      navController = navController,
      sharedTransitionScope = sharedTransitionScope,
    )
  }
}

fun NavController.navigateToHome() {
  navigate(HOME_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

private const val HOME_DESTINATION = "home"
const val HOME_NAVIGATION_ROUTE = "home_route"
