package com.mataku.scrobscrob.scrobble.ui.navigation

import com.mataku.scrobscrob.scrobble.ui.screen.TrackScreen
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

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
