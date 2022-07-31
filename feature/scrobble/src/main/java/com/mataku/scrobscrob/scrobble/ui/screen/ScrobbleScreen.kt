package com.mataku.scrobscrob.scrobble.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleScreenState
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleTopBarState
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

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
                Text(
                    text = stringResource(id = R.string.menu_scrobble),
                    style = SunsetTextStyle.h6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Colors.ContentBackground
                        )
                        .padding(16.dp)
                )
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
            .background(Colors.ContentBackground)
            .fillMaxSize()
    )
}