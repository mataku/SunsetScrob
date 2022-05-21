package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.state.TopAlbumsScreenState
import com.mataku.scrobscrob.album.ui.state.rememberTopAlbumsScreenState
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun TopAlbumsScreen(
    state: TopAlbumsScreenState = rememberTopAlbumsScreenState()
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopAlbumsContent(
    albums: List<Album>,
    hasNext: Boolean,
    imageSize: Dp,
    padding: Dp,
    onUrlTap: (String) -> Unit,
    onScrollEnd: () -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        content = {
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
        },
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        modifier = Modifier
            .background(
                Colors.ContentBackground
            )
            .fillMaxHeight()
    )
}