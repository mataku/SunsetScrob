package com.mataku.scrobscrob.app.ui.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.viewmodel.TopArtistsViewModel

class TopArtistsScreenState(
    val navController: NavController,
    val viewModel: TopArtistsViewModel,
    val context: Context
) {
    val uiState = viewModel.uiState

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
    navController: NavController = rememberNavController(),
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