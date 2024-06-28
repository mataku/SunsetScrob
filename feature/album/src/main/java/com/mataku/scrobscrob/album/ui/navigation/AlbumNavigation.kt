package com.mataku.scrobscrob.album.ui.navigation

import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mataku.scrobscrob.album.ui.screen.AlbumScreen
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.albumGraph(navController: NavController) {
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
  val encodedAlbumName = Uri.encode(albumName)
  val encodedArtistName = Uri.encode(artistName)
  return "${ALBUM_INFO_DESTINATION}?albumName=${encodedAlbumName}&artistName=${encodedArtistName}&artworkUrl=${artworkUrl}"
}

private const val TOP_ALBUMS_DESTINATION = "top_albums"
private const val ALBUM_INFO_DESTINATION = "album_info"
private const val ALBUM_NAVIGATION_ROUTE = "album_route"
