package com.mataku.scrobscrob.app.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.viewmodel.TopArtistsViewModel

class TopArtistsScreenState(
    val navController: NavController,
    val viewModel: TopArtistsViewModel
) {
    val uiState = viewModel.uiState

    fun onTapArtist(url: String) {
        navController.navigate("webview?url=$url")
    }

    fun onScrollEnd() {
        viewModel.fetchTopArtists()
    }
}

@Composable
fun rememberTopArtistsScreenState(
    viewModel: TopArtistsViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
): TopArtistsScreenState {
    return remember {
        TopArtistsScreenState(
            navController = navController,
            viewModel = viewModel
        )
    }
}