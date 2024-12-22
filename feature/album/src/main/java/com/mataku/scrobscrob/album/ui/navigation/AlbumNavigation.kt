package com.mataku.scrobscrob.album.ui.navigation

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
import com.mataku.scrobscrob.album.ui.screen.AlbumScreen
import com.mataku.scrobscrob.ui_common.navigateToWebView

fun NavGraphBuilder.albumGraph(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
) {
  composable(
    "${ALBUM_INFO_DESTINATION}?albumName={albumName}&artistName={artistName}&artworkUrl={artworkUrl}&id={id}",
    arguments = listOf(
      navArgument("artworkUrl") {
        type = NavType.StringType
      },
      navArgument("albumName") {
        type = NavType.StringType
      },
      navArgument("artistName") {
        type = NavType.StringType
      },
      navArgument("id") {
        type = NavType.StringType
      }
    ),
    content = {
      val contentId = it.arguments?.getString("id", "") ?: ""

      with(sharedTransitionScope) {
        AlbumScreen(
          viewModel = hiltViewModel(),
          onAlbumLoadMoreTap = { url ->
            if (url.isNotEmpty()) {
              navController.navigateToWebView(url)
            }
          },
          onBackPressed = navController::popBackStack,
          animatedContentScope = this@composable,
          id = contentId,
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

fun NavController.navigateToAlbumInfo(
  albumName: String,
  artistName: String,
  artworkUrl: String,
  contentId: String,
) {
  val destination = buildAlbumInfoUrl(
    albumName = albumName,
    artistName = artistName,
    artworkUrl = artworkUrl,
    contentId = contentId
  )
  navigate(destination)
}

private fun buildAlbumInfoUrl(
  albumName: String,
  artistName: String,
  artworkUrl: String,
  contentId: String,
): String {
  val encodedAlbumName = Uri.encode(albumName)
  val encodedArtistName = Uri.encode(artistName)
  val encodedContentId = Uri.encode(contentId)
  return "${ALBUM_INFO_DESTINATION}?albumName=${encodedAlbumName}&artistName=${encodedArtistName}&artworkUrl=${artworkUrl}&id=${encodedContentId}"
}

private const val ALBUM_INFO_DESTINATION = "album_detail"
