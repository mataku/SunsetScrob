package com.mataku.scrobscrob.scrobble.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.scrobble.ui.navigation.navigateToTrackDetail
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel

class ScrobbleScreenState(
  private val navController: NavController,
  private val viewModel: ScrobbleViewModel
) {
  val uiState: ScrobbleViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  fun onScrollEnd() {
    viewModel.fetchRecentTracks()
  }

  fun refresh() {
    viewModel.refresh()
  }

  fun navigateToTrackDetail(
    trackName: String,
    artistName: String,
    imageUrl: String,
    x: Int,
    y: Int
  ) {
    navController.navigateToTrackDetail(
      trackName, artistName, imageUrl, x, y
    )
  }
}

@Composable
fun rememberScrobbleScreenState(
  navController: NavController,
  viewModel: ScrobbleViewModel = hiltViewModel()
): ScrobbleScreenState {
  return remember(navController, viewModel) {
    ScrobbleScreenState(
      navController = navController,
      viewModel = viewModel
    )
  }
}
