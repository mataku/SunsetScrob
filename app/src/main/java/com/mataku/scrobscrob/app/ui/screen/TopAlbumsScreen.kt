package com.mataku.scrobscrob.app.ui.screen

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.molecule.TopAlbum
import com.mataku.scrobscrob.app.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopAlbumsScreen(navController: NavController, viewModel: TopAlbumsViewModel) {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val fullWidth = displayMetrics.widthPixels / displayMetrics.density
    val halfWidth = fullWidth / 2
    val uiState = viewModel.uiState

    if (uiState.topAlbums.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.ContentBackground)
        )
    } else {
        TopAlbumsContent(
            albums = uiState.topAlbums,
            hasNext = uiState.hasNext,
            imageSize = halfWidth.dp,
            padding = halfWidth.dp - 20.dp,
            onUrlTap = {
                navController.navigate(
                    "webview?url=$it"
                )
            },
            onScrollEnd = {
                viewModel.fetchAlbums()
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
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