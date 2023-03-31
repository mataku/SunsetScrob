package com.mataku.scrobscrob.artist.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.artist.R
import com.mataku.scrobscrob.artist.ui.molecule.TopArtist
import com.mataku.scrobscrob.artist.ui.state.TopArtistsScreenState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.ui_common.molecule.FilteringFloatingButton
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.FilteringBottomSheet
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.sunsetBackgroundGradient
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopArtistsScreen(
  state: TopArtistsScreenState
) {
  val contentWidth = state.contentWidth
  val uiState = state.uiState

  val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
  val coroutineScope = rememberCoroutineScope()
  BackHandler(sheetState.isVisible) {
    coroutineScope.launch {
      sheetState.hide()
    }
  }
  ModalBottomSheetLayout(
    sheetContent = {
      FilteringBottomSheet(
        selectedTimeRangeFiltering = uiState.selectedTimeRangeFiltering,
        onClick = {
          coroutineScope.launch {
            sheetState.hide()
          }
          state.updateTimeRange(it)
        },
        modifier = Modifier
          .padding(bottom = 56.dp)
      )
    },
    sheetState = sheetState,
    sheetBackgroundColor = MaterialTheme.colorScheme.background
  ) {
    Scaffold(
      floatingActionButton = {
        FilteringFloatingButton(
          onClick = {
            coroutineScope.launch {
              sheetState.show()
            }
          },
          modifier = Modifier
            .padding(bottom = 56.dp)
        )
      }
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

  if (uiState.isLoading && uiState.topArtists.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      LoadingIndicator(
        modifier = Modifier
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TopArtistsContent(
  artists: ImmutableSet<ArtistInfo>,
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
      if (hasNext && artists.isNotEmpty()) {
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
