package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.app.model.DummyAlbums
import com.mataku.scrobscrob.app.ui.molecule.TopAlbum
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun TopAlbumsScreen() {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val halfWidth = displayMetrics.widthPixels / (2 * displayMetrics.density)

    TopAlbumsContent(albums = DummyAlbums.contents, imageSize = halfWidth.dp)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopAlbumsContent(albums: List<Album>, imageSize: Dp) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        content = {
            items(albums) {
                TopAlbum(album = it, imageSize = imageSize)
            }
        },
        modifier = Modifier
            .background(Colors.ContentBackground)
            .fillMaxHeight()
    )
}