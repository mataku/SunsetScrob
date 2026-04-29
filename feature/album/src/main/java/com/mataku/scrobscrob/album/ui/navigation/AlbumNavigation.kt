package com.mataku.scrobscrob.album.ui.navigation

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
import com.mataku.scrobscrob.album.ui.screen.AlbumScreen
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

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
      // TODO: Phase 7 cleanup — old graph body disabled after AlbumViewModel migrated to NavKey injection
      TODO("Phase 7 cleanup: use SunsetNavBuilder.albumGraph() instead")
    },
    enterTransition = {
      fadeIn(tween(300))
    },
    exitTransition = {
      fadeOut(tween(250))
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

fun SunsetNavBuilder.albumGraph() {
  destination<AlbumKey> { key ->
    AlbumScreen(
      viewModel = viewModelFor<AlbumViewModel>(key),
      onAlbumLoadMoreTap = { url -> if (url.isNotEmpty()) navigate(WebViewKey(url)) },
      onBackPressed = ::popBackStack,
      animatedContentScope = animatedContentScope,
      id = key.contentId,
    )
  }
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
