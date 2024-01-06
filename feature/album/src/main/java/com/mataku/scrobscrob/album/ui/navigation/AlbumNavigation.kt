package com.mataku.scrobscrob.album.ui.navigation

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
import androidx.navigation.navigation
import com.mataku.scrobscrob.album.ui.screen.AlbumScreen
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.albumGraph(navController: NavController) {
  navigation(route = ALBUM_NAVIGATION_ROUTE, startDestination = TOP_ALBUMS_DESTINATION) {
    composable(
      TOP_ALBUMS_DESTINATION
    ) {
      TopAlbumsScreen(
        viewModel = hiltViewModel(),
        navigateToAlbumInfo = { album ->
          navController.navigateToAlbumInfo(
            albumName = album.title,
            artistName = album.artist,
            artworkUrl = album.imageList.imageUrl() ?: ""
          )
        }
      )
    }

    composable(
      "${ALBUM_INFO_DESTINATION}?albumName={albumName}&artistName={artistName}&artworkUrl={artworkUrl}",
      arguments = listOf(
        navArgument("artworkUrl") {
          type = NavType.StringType
        },
        navArgument("albumName") {
          type = NavType.StringType
        },
        navArgument("artistName") {
          type = NavType.StringType
        }
      ),
      enterTransition = {
        fadeIn(tween(300))
      },
      exitTransition = {
        fadeOut(tween(300))
      }
    ) {
      AlbumScreen(
        viewModel = hiltViewModel(),
        onAlbumLoadMoreTap = {
          if (it.isNotEmpty()) {
            navController.navigateToWebView(it)
          }
        }
      )
    }
  }
}

fun NavController.navigateToTopAlbums() {
  navigate(ALBUM_NAVIGATION_ROUTE) {
    popUpTo(graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

fun NavController.navigateToAlbumInfo(
  albumName: String,
  artistName: String,
  artworkUrl: String,
) {
  val destination = buildAlbumInfoUrl(
    albumName = albumName,
    artistName = artistName,
    artworkUrl = artworkUrl
  )
  navigate(destination)
}

private fun buildAlbumInfoUrl(
  albumName: String,
  artistName: String,
  artworkUrl: String,
): String {
  return "${ALBUM_INFO_DESTINATION}?albumName=${albumName}&artistName=${artistName}&artworkUrl=${artworkUrl}"
}

private const val TOP_ALBUMS_DESTINATION = "top_albums"
private const val ALBUM_INFO_DESTINATION = "album_info"
private const val ALBUM_NAVIGATION_ROUTE = "album_route"
