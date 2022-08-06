package com.mataku.scrobscrob.scrobble.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel

class TrackScreenState(
  private val navController: NavController,
  private val viewModel: TrackViewModel,
  private val trackName: String,
  private val artistName: String,
) {

  val uiState: TrackViewModel.UiState
    @Composable get() = viewModel.state.collectAsState().value

  init {
    viewModel.fetchTrackInfo(
      trackName = trackName,
      artistName = artistName
    )
  }

  fun clearState() {
    viewModel.clearState()
  }

  fun popBackStack() {
    navController.popBackStack()
  }

  sealed class UiEvent {
    object TrackInfoFetchFailure : UiEvent()
  }
}

@Composable
fun rememberTrackScreenState(
  navController: NavController,
  viewModel: TrackViewModel = hiltViewModel(),
  trackName: String,
  artistName: String
): TrackScreenState =
  remember {
    TrackScreenState(
      navController = navController,
      viewModel = viewModel,
      trackName = trackName,
      artistName = artistName
    )
  }
