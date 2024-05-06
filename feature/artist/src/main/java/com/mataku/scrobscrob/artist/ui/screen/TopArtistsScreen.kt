package com.mataku.scrobscrob.artist.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.artist.ui.molecule.TopArtist
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.ui_common.molecule.FilteringFloatingButton
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.organism.FilteringBottomSheet
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopArtistsScreen(
  viewModel: TopArtistsViewModel,
  onArtistTap: (TopArtistInfo) -> Unit,
  topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
  val uiState by viewModel.uiState.collectAsState()

  val bottomSheetState = rememberModalBottomSheetState()
  var showBottomSheet by remember {
    mutableStateOf(false)
  }
  val coroutineScope = rememberCoroutineScope()
  val configuration = LocalConfiguration.current
  val orientation = remember {
    configuration.orientation
  }
  val density = LocalDensity.current

  val topAppBarHeightPixel by remember {
    derivedStateOf {
      topAppBarScrollBehavior.state.heightOffset
    }
  }
  BackHandler(bottomSheetState.isVisible) {
    coroutineScope.launch {
      bottomSheetState.hide()
    }
  }
  Scaffold(
    floatingActionButton = {
      FilteringFloatingButton(
        onClick = {
          coroutineScope.launch {
            showBottomSheet = true
          }
        },
        modifier = Modifier
          .padding(
            bottom = 152.dp + with(density) {
              topAppBarHeightPixel.toDp()
            }
          )
      )
    }
  ) {
    TopArtistsContent(
      artists = uiState.topArtists,
      hasNext = uiState.hasNext,
      onArtistTap = onArtistTap,
      onScrollEnd = viewModel::fetchTopArtists,
      maxSpanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        4
      } else {
        2
      },
      modifier = Modifier
        .fillMaxSize()
        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    )
    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = {
          showBottomSheet = false
        },
        sheetState = bottomSheetState,
        modifier = Modifier,
        windowInsets = WindowInsets.displayCutout
      ) {
        FilteringBottomSheet(
          selectedTimeRangeFiltering = uiState.selectedTimeRangeFiltering,
          onClick = {
            viewModel.updateTimeRange(it)
            coroutineScope.launch {
              bottomSheetState.hide()
            }.invokeOnCompletion {
              showBottomSheet = false
            }
          }
        )
      }
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
  artists: ImmutableList<TopArtistInfo>,
  hasNext: Boolean,
  maxSpanCount: Int,
  onArtistTap: (TopArtistInfo) -> Unit,
  onScrollEnd: () -> Unit,
  modifier: Modifier = Modifier
) {
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
            onArtistTap.invoke(artist)
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
    },
    modifier = modifier
  )
}
