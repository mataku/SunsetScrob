package com.mataku.scrobscrob.scrobble.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel

class ScrobbleScreenState(
    val navController: NavHostController,
    private val viewModel: ScrobbleViewModel
) {
    val uiState: ScrobbleViewModel.UiState
        @Composable get() = viewModel.uiState.collectAsState().value

    fun onScrobbleTap(url: String) {
        navController.navigate("webview?url=$url")
    }

    fun onScrollEnd() {
        viewModel.fetchRecentTracks()
    }

    fun refresh() {
        viewModel.refresh()
    }
}

@Composable
fun rememberScrobbleScreenState(
    navController: NavHostController,
    viewModel: ScrobbleViewModel = hiltViewModel()
): ScrobbleScreenState {
    return remember(navController, viewModel) {
        ScrobbleScreenState(
            navController = navController,
            viewModel = viewModel
        )
    }
}