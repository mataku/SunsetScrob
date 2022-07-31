package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.album.R
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.state.TopAlbumsScreenState
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator

@Composable
fun TopAlbumsScreen(
    state: TopAlbumsScreenState
) {
    val uiState = state.uiState

    if (uiState.topAlbums.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
    LazyColumn(
        content = {
            stickyHeader {
                ContentHeader(text = stringResource(id = R.string.menu_top_albums))
            }

            items(albums.chunked(2)) {
                val rightItem = if (it.size == 1) null else it[1]
                TopAlbumsGridRow(
                    leftItem = it[0],
                    rightItem = rightItem,
                    imageSize = imageSize,
                    onAlbumTap = onUrlTap,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            if (hasNext) {
                item {
                    InfiniteLoadingIndicator(
                        onScrollEnd = onScrollEnd,
                        padding = padding
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 56.dp)
    )
}

@Composable
private fun TopAlbumsGridRow(
    leftItem: Album,
    rightItem: Album?,
    imageSize: Dp,
    onAlbumTap: (String) -> Unit,
    modifier: Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        TopAlbum(
            album = leftItem, imageSize = imageSize, onAlbumTap = {
                onAlbumTap(leftItem.url)
            },
            modifier = Modifier.weight(1F)
        )
        rightItem?.let {
            TopAlbum(
                album = it, imageSize = imageSize, onAlbumTap = {
                    onAlbumTap(it.url)
                },
                modifier = Modifier.weight(1F)

            )
        }
    }
}