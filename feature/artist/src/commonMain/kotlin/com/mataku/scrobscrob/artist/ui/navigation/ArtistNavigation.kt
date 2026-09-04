package com.mataku.scrobscrob.artist.ui.navigation

import com.mataku.scrobscrob.artist.ui.screen.ArtistScreen
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.SunsetTransitionSpec
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.artistGraph() {
  destination<ArtistKey>(transitionSpec = SunsetTransitionSpec.SharedElement) { key ->
    ArtistScreen(
      id = key.contentId,
      animatedVisibilityScope = animatedContentScope,
      viewModel = viewModelFor<ArtistViewModel>(key),
      onArtistLoadMoreTap = { url -> navigate(WebViewKey(url)) },
      onBackPressed = ::popBackStack,
    )
  }
}
