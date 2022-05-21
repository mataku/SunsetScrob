package com.mataku.scrobscrob.artist.ui.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel

class TopArtistsScreenState(
    private val navController: NavHostController,
    private val viewModel: TopArtistsViewModel,
    context: Context
) {
    val uiState: TopArtistsViewModel.UiState
        @@Composable get() = viewModel.uiState.collectAsState().value

    private val displayMetrics = context.resources.displayMetrics
    private val fullWidth = displayMetrics.widthPixels / displayMetrics.density
    val contentWidth = fullWidth / 2

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
    navController: NavHostController,
    context: Context = LocalContext.current
): TopArtistsScreenState {
    return remember {
        TopArtistsScreenState(
            navController = navController,
            viewModel = viewModel,
            context = context
        )
    }
}