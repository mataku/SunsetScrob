package com.mataku.scrobscrob.album.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.isInvalidArtwork
import com.mataku.scrobscrob.ui_common.molecule.FilteringFloatingButton
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.organism.FilteringBottomSheet
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAlbumsScreen(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  viewModel: TopAlbumsViewModel,
  navigateToAlbumInfo: (TopAlbumInfo, String) -> Unit,
  topAppBarScrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

  val topAppBarHeightPixel by remember {
    derivedStateOf {
      topAppBarScrollBehavior.state.heightOffset
    }
  }
  val density = LocalDensity.current
  Scaffold(
    floatingActionButton = {
      FilteringFloatingButton(
        onClick = {
          coroutineScope.launch {
            showBottomSheet = true
          }
        },
        modifier = modifier
          .padding(
            bottom = 152.dp + with(density) {
              topAppBarHeightPixel.toDp()
            }
          )
      )
    }
  ) {
    TopAlbumsContent(
      sharedTransitionScope = sharedTransitionScope,
      animatedContentScope = animatedContentScope,
      albums = uiState.topAlbums,
      hasNext = uiState.hasNext,
      onScrollEnd = viewModel::fetchAlbums,
      maxSpanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        4
      } else {
        2
      },
      onAlbumTap = navigateToAlbumInfo,
      modifier = Modifier
        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    )

    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = {
          showBottomSheet = false
        },
        sheetState = bottomSheetState,
        contentWindowInsets = {
          WindowInsets.displayCutout
        },
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
          },
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
private fun TopAlbumsContent(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  albums: ImmutableList<TopAlbumInfo>,
  hasNext: Boolean,
  maxSpanCount: Int,
  onAlbumTap: (TopAlbumInfo, String) -> Unit,
  onScrollEnd: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyVerticalGrid(
    contentPadding = PaddingValues(8.dp),
    columns = GridCells.Fixed(maxSpanCount),
    content = {
      itemsIndexed(
        items = albums,
        key = { _, album ->
          "${album.hashCode()}"
        },
        contentType = { _, _ ->
          "top_albums"
        }
      ) { index, album ->
        val id = if (album.imageList.imageUrl().isInvalidArtwork()) {
          ""
        } else {
          "top_album_${index}${album.hashCode()}"
        }
        TopAlbum(
          album = album,
          onAlbumTap = {
            onAlbumTap.invoke(album, id)
          },
          sharedTransitionScope = sharedTransitionScope,
          animatedContentScope = animatedContentScope,
          id = id
        )
      }

      if (hasNext && albums.isNotEmpty()) {
        item(
          key = "top_albums_loading",
          span = {
            GridItemSpan(maxLineSpan)
          },
        ) {
          InfiniteLoadingIndicator(
            onScrollEnd = onScrollEnd,
            modifier = Modifier
          )
        }
      }
    },
    modifier = modifier
      .fillMaxSize()
  )
}
