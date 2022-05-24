package com.mataku.scrobscrob.album.ui.screen

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
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.state.TopAlbumsScreenState
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun TopAlbumsScreen(
    state: TopAlbumsScreenState
) {
    val uiState = state.uiState

    if (uiState.topAlbums.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.ContentBackground)
        )
    } else {
        val contentWidth = state.contentWidth
        TopAlbumsContent(
            albums = uiState.topAlbums,
            hasNext = uiState.hasNext,
            imageSize = contentWidth.dp,
            padding = contentWidth.dp - 20.dp,
            onUrlTap = {
                state.onTapAlbum(it)
            },
            onScrollEnd = {
                state.onScrollEnd()
            }
        )
    }
}

@Composable
fun TopAlbumsContent(
    albums: List<Album>,
    hasNext: Boolean,
    imageSize: Dp,
    padding: Dp,
    onUrlTap: (String) -> Unit,
    onScrollEnd: () -> Unit
) = LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = Modifier
        .background(
            Colors.ContentBackground
        )
        .fillMaxHeight(),
    contentPadding = PaddingValues(start = 8.dp, end = 8.dp)
) {
    items(albums) {
        TopAlbum(album = it, imageSize = imageSize, onAlbumTap = {
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
}