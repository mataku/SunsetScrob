package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.molecule.ScrobbleTopBar
import com.mataku.scrobscrob.app.ui.molecule.ScrobbleView
import com.mataku.scrobscrob.app.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun ScrobbleScreen(
    navController: NavController,
    viewModel: ScrobbleViewModel
) {
    val uiState = viewModel.uiState
    Scaffold(
        topBar = {
            ScrobbleTopBar(navController = navController)
        },
        bottomBar = {

        }
    ) {
        ScrobbleContent(
            recentTracks = uiState.recentTracks,
            hasNext = uiState.hasNext,
            onScrobbleTap = {
                navController.navigate(
                    "webview?url=$it"
                )
            },
            onScrollEnd = {
                viewModel.fetchRecentTracks()
            }
        )
    }
}

@Composable
private fun ScrobbleContent(
    recentTracks: List<RecentTrack>,
    hasNext: Boolean,
    onScrobbleTap: (String) -> Unit,
    onScrollEnd: () -> Unit
) {
    LazyColumn(
        content = {
            items(recentTracks) {
                ScrobbleView(
                    recentTrack = it,
                    onScrobbleTap = {
                        onScrobbleTap(it.url)
                    }
                )
            }
            if (hasNext) {
                item {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        InfiniteLoadingIndicator(onScrollEnd = onScrollEnd)
                    }
                }
            }
        },
        modifier = Modifier
            .background(Colors.ContentBackground)
            .fillMaxSize()
    )
}