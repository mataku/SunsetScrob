package com.mataku.scrobscrob.home.ui.navigation

import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.album.ui.navigation.AlbumKey
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.home.ui.screen.HomeScreen
import com.mataku.scrobscrob.home.ui.viewmodel.HomeViewModel
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.homeGraph() {
  destination<HomeKey> {
    HomeScreen(
      viewModel = viewModelFor<HomeViewModel>(HomeKey),
      sharedTransitionScope = this,
      animatedContentScope = animatedContentScope,
      navigateToTrackDetail = { track, id ->
        navigate(
          TrackDetailKey(
            trackName = track.name,
            artistName = track.artistName,
            imageUrl = track.images.imageUrl() ?: "",
            id = id,
          ),
        )
      },
      trackViewModelProvider = { key -> viewModelFor<TrackViewModel>(key) },
      navigateToWebView = { url -> navigate(WebViewKey(url)) },
      albumViewModelProvider = { key -> viewModelFor<AlbumViewModel>(key) },
      navigateToArtistDetail = { artist, id ->
        navigate(
          ArtistKey(
            artistName = artist.name,
            artworkUrl = (artist.imageUrl ?: artist.imageList.imageUrl()) ?: "",
            contentId = id,
          ),
        )
      },
      navigateToAlbumDetail = { album, id ->
        navigate(
          AlbumKey(
            albumName = album.title,
            artistName = album.artist,
            artworkUrl = album.imageList.imageUrl() ?: "",
            contentId = id,
          ),
        )
      },
      navigateToLogin = { /* 認証 gate 切替で処理 */ },
      modifier = Modifier,
    )
  }
}
