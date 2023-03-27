package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.artist.R
import com.mataku.scrobscrob.artist.ui.molecule.TopArtist
import com.mataku.scrobscrob.artist.ui.state.TopArtistsScreenState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.sunsetBackgroundGradient

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopArtistsScreen(
  state: TopArtistsScreenState
) {
  val contentWidth = state.contentWidth
  val uiState = state.uiState

  val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
  ModalBottomSheetLayout(
    sheetContent = {

    },
    sheetState = sheetState
  ) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TopArtistsContent(
  artists: List<ArtistInfo>,
  hasNext: Boolean,
  imageSize: Dp,
  padding: Dp,
  onUrlTap: (String) -> Unit,
  onScrollEnd: () -> Unit
) {
  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = stringResource(id = R.string.menu_top_artists))
      }

      items(artists.chunked(2)) {
        val rightItem = if (it.size == 1) null else it[1]
        TopArtistsGridRow(
          leftArtist = it[0],
          rightArtist = rightItem,
          imageSize = imageSize - 24.dp,
          onArtistTap = onUrlTap,
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
    modifier = if (LocalAppTheme.current == AppTheme.SUNSET) {
      Modifier
        .fillMaxSize()
        .background(
          brush = sunsetBackgroundGradient
        )
        .padding(bottom = 56.dp)
    } else {
      Modifier
        .fillMaxSize()
        .padding(bottom = 56.dp)
    }

  )
}

@Composable
private fun TopArtistsGridRow(
  leftArtist: ArtistInfo,
  rightArtist: ArtistInfo?,
  imageSize: Dp,
  onArtistTap: (String) -> Unit,
  modifier: Modifier
) {
  Row(modifier = modifier.fillMaxSize()) {
    TopArtist(
      artist = leftArtist,
      imageSize = imageSize,
      onArtistTap = {
        onArtistTap.invoke(leftArtist.url)
      },
      modifier = Modifier.weight(1F, fill = false)
    )
    rightArtist?.let {
      TopArtist(
        artist = it,
        imageSize = imageSize,
        onArtistTap = {
          onArtistTap.invoke(it.url)
        },
        modifier = Modifier.weight(1F, fill = false)
      )
    }
  }
}
