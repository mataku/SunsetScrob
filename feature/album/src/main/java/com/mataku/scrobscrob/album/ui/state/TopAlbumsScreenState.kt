package com.mataku.scrobscrob.album.ui.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.ui_common.navigateToWebView
import org.koin.androidx.compose.koinViewModel

class TopAlbumsScreenState(
  private val navController: NavController,
  private val viewModel: TopAlbumsViewModel,
  context: Context
) {
  val uiState: TopAlbumsViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  private val displayMetrics = context.resources.displayMetrics
  private val fullWidth = displayMetrics.widthPixels / displayMetrics.density
  val contentWidth = fullWidth / 2

  fun onTapAlbum(url: String) {
    navController.navigateToWebView(url)
  }

  fun onScrollEnd() {
    viewModel.fetchAlbums()
  }
}

@Composable
fun rememberTopAlbumsScreenState(
  viewModel: TopAlbumsViewModel = koinViewModel(),
  navController: NavController,
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
