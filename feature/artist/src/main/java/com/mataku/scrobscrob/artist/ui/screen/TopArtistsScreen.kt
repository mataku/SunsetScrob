package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.artist.ui.molecule.TopArtist
import com.mataku.scrobscrob.artist.ui.state.TopArtistsScreenState
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun TopArtistsScreen(
    state: TopArtistsScreenState
) {
    val contentWidth = state.contentWidth
    val uiState = state.uiState
    if (uiState.topArtists.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.ContentBackground)
        )
    } else {
        TopArtistsContent(
            artists = uiState.topArtists,
            hasNext = uiState.hasNext,
            imageSize = contentWidth.dp,
            padding = contentWidth.dp - 20.dp,
            onUrlTap = {
                state.onTapArtist(it)
            },
            onScrollEnd = {
                state.onScrollEnd()
            }
        )
    }
}

@Composable
private fun TopArtistsContent(
    artists: List<Artist>,
    hasNext: Boolean,
    imageSize: Dp,
    padding: Dp,
    onUrlTap: (String) -> Unit,
    onScrollEnd: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        content = {
            items(artists) {
                TopArtist(artist = it, imageSize = imageSize, onArtistTap = {
                    onUrlTap(it.url)
                })
            }
            if (hasNext) {
                item(span = { GridItemSpan(2) }) {
                    InfiniteLoadingIndicator(
                        onScrollEnd = onScrollEnd,
                        padding = padding
                    )
                }
            }
        },
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        modifier = Modifier
            .background(
                Colors.ContentBackground
            )
            .fillMaxHeight()
    )
}