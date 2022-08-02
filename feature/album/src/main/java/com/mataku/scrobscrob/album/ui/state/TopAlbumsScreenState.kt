package com.mataku.scrobscrob.album.ui.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel

class TopAlbumsScreenState(
  private val navController: NavHostController,
  private val viewModel: TopAlbumsViewModel,
  context: Context
) {
  val uiState: TopAlbumsViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  private val displayMetrics = context.resources.displayMetrics
  private val fullWidth = displayMetrics.widthPixels / displayMetrics.density
  val contentWidth = fullWidth / 2

  fun onTapAlbum(url: String) {
    navController.navigate("webview?url=$url")
  }

  fun onScrollEnd() {
    viewModel.fetchAlbums()
  }
}

@Composable
fun rememberTopAlbumsScreenState(
  viewModel: TopAlbumsViewModel = hiltViewModel(),
  navController: NavHostController,
  context: Context = LocalContext.current
): TopAlbumsScreenState {
  return remember {
    TopAlbumsScreenState(
      navController = navController,
      viewModel = viewModel,
      context = context
    )
  }
}
