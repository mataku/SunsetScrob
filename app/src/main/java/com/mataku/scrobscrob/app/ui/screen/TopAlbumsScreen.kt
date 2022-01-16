package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.molecule.TopAlbum
import com.mataku.scrobscrob.app.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.style.Colors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopAlbumsScreen(navController: NavController, viewModel: TopAlbumsViewModel) {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val halfWidth = displayMetrics.widthPixels / (2 * displayMetrics.density)
    val uiState = viewModel.uiState
    TopAlbumsContent(
        albums = uiState.topAlbums,
        hasNext = uiState.hasNext,
        imageSize = halfWidth.dp,
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TopAlbumsContent(
    albums: List<Album>,
    hasNext: Boolean,
    imageSize: Dp,
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
                items(2) {
                    LoadingIndicator(
                        onScrollEnd = onScrollEnd
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

@Composable
private fun LoadingIndicator(onScrollEnd: () -> Unit) {
    CircularProgressIndicator()

    LaunchedEffect(key1 = true, block = {
        onScrollEnd()
    })
}