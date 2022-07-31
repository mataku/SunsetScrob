package com.mataku.scrobscrob.scrobble.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleScreenState
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleTopBarState
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScrobbleScreen(
    state: ScrobbleScreenState,
    topBarState: ScrobbleTopBarState
) {
    val uiState = state.uiState
    Scaffold(
        topBar = {
//            ScrobbleTopBar(stateHolder = topBarState)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrobbleContent(
    recentTracks: List<RecentTrack>,
    hasNext: Boolean,
    onScrobbleTap: (String) -> Unit,
    onScrollEnd: () -> Unit
) {

    LazyColumn(
        content = {
            stickyHeader {
                ContentHeader(text = stringResource(id = R.string.menu_scrobble))
            }

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
            .fillMaxSize()
    )
}