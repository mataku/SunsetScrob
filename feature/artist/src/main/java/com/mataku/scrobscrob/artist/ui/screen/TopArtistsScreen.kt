package com.mataku.scrobscrob.artist.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopArtistsScreen(
  state: TopArtistsScreenState
) {
  val uiState = state.uiState

  val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
  val coroutineScope = rememberCoroutineScope()
  val configuration = LocalConfiguration.current
  val orientation = remember {
    configuration.orientation
  }
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
        )
      }
    ) {
      TopArtistsContent(
        artists = uiState.topArtists,
        hasNext = uiState.hasNext,
        onUrlTap = {
          state.onTapArtist(it)
        },
        onScrollEnd = {
          state.onScrollEnd()
        },
        maxSpanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
          4
        } else {
          2
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

@Composable
private fun TopArtistsContent(
  artists: ImmutableList<ArtistInfo>,
  hasNext: Boolean,
  maxSpanCount: Int,
  onUrlTap: (String) -> Unit,
  onScrollEnd: () -> Unit
) {
  Column(
    modifier = if (LocalAppTheme.current == AppTheme.SUNSET) {
      Modifier
        .fillMaxSize()
        .background(
          brush = sunsetBackgroundGradient
        )
    } else {
      Modifier
        .fillMaxSize()
    }
  ) {
    ContentHeader(text = stringResource(id = R.string.menu_top_artists))

    LazyVerticalGrid(
      contentPadding = PaddingValues(horizontal = 8.dp),
      columns = GridCells.Fixed(maxSpanCount),
      content = {
        itemsIndexed(
          items = artists,
          key = { index, artist ->
            "${index}${artist.hashCode()}"
          }
        ) { _, artist ->
          TopArtist(
            artist = artist,
            onArtistTap = {
              onUrlTap.invoke(artist.url)
            },
            modifier = Modifier
              .fillMaxWidth()
          )
        }

        if (hasNext && artists.isNotEmpty()) {
          item(span = { GridItemSpan(2) }) {
            InfiniteLoadingIndicator(
              onScrollEnd = onScrollEnd,
              modifier = Modifier
            )
          }
        }
      })
  }
}
