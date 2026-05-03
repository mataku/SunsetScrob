package com.mataku.scrobscrob.album.ui.navigation

import com.mataku.scrobscrob.album.ui.screen.AlbumScreen
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.SunsetTransitionSpec
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.albumGraph() {
  destination<AlbumKey>(transitionSpec = SunsetTransitionSpec.SharedElement) { key ->
    AlbumScreen(
      viewModel = viewModelFor<AlbumViewModel>(key),
      onAlbumLoadMoreTap = { url -> if (url.isNotEmpty()) navigate(WebViewKey(url)) },
      onBackPressed = ::popBackStack,
      animatedVisibilityScope = animatedContentScope,
      id = key.contentId,
    )
  }
}
