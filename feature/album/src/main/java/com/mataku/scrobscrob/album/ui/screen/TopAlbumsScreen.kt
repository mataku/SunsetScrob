package com.mataku.scrobscrob.album.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mataku.scrobscrob.album.R
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.ui_common.molecule.FilteringFloatingButton
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.navigateToWebView
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.FilteringBottomSheet
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAlbumsScreen(
  viewModel: TopAlbumsViewModel,
  navController: NavController
) {
  val uiState by viewModel.uiState.collectAsState()

  var showBottomSheet by remember {
    mutableStateOf(false)
  }
  val bottomSheetState = rememberModalBottomSheetState()
  val coroutineScope = rememberCoroutineScope()
  val configuration = LocalConfiguration.current
  val orientation = remember {
    configuration.orientation
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
            bottom = 80.dp
          )
      )
    }
  ) {
    TopAlbumsContent(
      albums = uiState.topAlbums,
      hasNext = uiState.hasNext,
      onUrlTap = navController::navigateToWebView,
      onScrollEnd = viewModel::fetchAlbums,
      maxSpanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        4
      } else {
        2
      }
    )

    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = {
          showBottomSheet = false
        },
        sheetState = bottomSheetState
      ) {
        FilteringBottomSheet(
          selectedTimeRangeFiltering = uiState.timeRangeFiltering,
          onClick = {
            coroutineScope.launch {
              bottomSheetState.hide()
            }.invokeOnCompletion {
              showBottomSheet = false
            }
            viewModel.updateTimeRange(it)
          }
        )
      }
    }
  }

  if (uiState.isLoading && uiState.topAlbums.isEmpty()) {
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
fun TopAlbumsContent(
  albums: ImmutableList<TopAlbumInfo>,
  hasNext: Boolean,
  maxSpanCount: Int,
  onUrlTap: (String) -> Unit,
  onScrollEnd: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
  ) {
    ContentHeader(text = stringResource(id = R.string.menu_top_albums))

    LazyVerticalGrid(
      contentPadding = PaddingValues(horizontal = 8.dp),
      columns = GridCells.Fixed(maxSpanCount),
      content = {
        items(albums) { album ->
          TopAlbum(
            album = album,
            onAlbumTap = {
              onUrlTap.invoke(album.url)
            },
            modifier = Modifier.fillMaxWidth(),
          )
        }

        if (hasNext && albums.isNotEmpty()) {
          item {
            InfiniteLoadingIndicator(
              onScrollEnd = onScrollEnd,
              modifier = Modifier
            )
          }
        }
      },
      modifier = Modifier
        .testTag("album_list")
    )
  }
}
