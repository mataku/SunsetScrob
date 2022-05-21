package com.mataku.scrobscrob.scrobble.ui.screen

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
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.molecule.ScrobbleTopBar
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleScreenState
import com.mataku.scrobscrob.scrobble.ui.state.rememberScrobbleScreenState
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun ScrobbleScreen(
    state: ScrobbleScreenState = rememberScrobbleScreenState()
) {
    val uiState = state.uiState
    Scaffold(
        topBar = {
            ScrobbleTopBar(navController = state.navController)
        },
        bottomBar = {}
    ) {
        ScrobbleContent(
            recentTracks = uiState.recentTracks,
            hasNext = uiState.hasNext,
            onScrobbleTap = {
                state.onScrobbleTap(it)
            },
            onScrollEnd = {
                state.onScrollEnd()
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
                Scrobble(
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