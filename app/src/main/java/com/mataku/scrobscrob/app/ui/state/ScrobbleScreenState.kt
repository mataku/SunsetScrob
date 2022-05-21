package com.mataku.scrobscrob.app.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.viewmodel.ScrobbleViewModel

class ScrobbleScreenState(
    val navController: NavController,
    private val viewModel: ScrobbleViewModel
) {
    val uiState = viewModel.uiState

    fun onScrobbleTap(url: String) {
        navController.navigate("webview?url=$url")
    }

    fun onScrollEnd() {
        viewModel.fetchRecentTracks()
    }
}

@Composable
fun rememberScrobbleScreenState(
    navController: NavController = rememberNavController(),
    viewModel: ScrobbleViewModel = hiltViewModel()
): ScrobbleScreenState {
    return remember {
        ScrobbleScreenState(
            navController = navController,
            viewModel = viewModel
        )
    }
}