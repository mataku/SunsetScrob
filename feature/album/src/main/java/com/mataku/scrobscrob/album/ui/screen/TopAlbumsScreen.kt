package com.mataku.scrobscrob.album.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.navigation.AlbumKey
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel.TopAlbumsUiState
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.isInvalidArtwork
import com.mataku.scrobscrob.ui_common.component.FilteringBottomSheet
import com.mataku.scrobscrob.ui_common.component.FilteringFloatingButton
import com.mataku.scrobscrob.ui_common.component.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.component.LoadingIndicator
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetListDetailScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetModalBottomSheet
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBarScrollBehavior
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetListDetailScaffoldState
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetModalBottomSheetState
import com.mataku.scrobscrob.ui_common.style.isCompactWidth
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopAlbumsScreen(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  viewModel: TopAlbumsViewModel,
  navigateToAlbumInfo: (TopAlbumInfo, String) -> Unit,
  albumViewModelProvider: @Composable (AlbumKey) -> AlbumViewModel,
  navigateToWebView: (String) -> Unit,
  topAppBarScrollBehavior: SunsetTopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  if (isCompactWidth()) {
    TopAlbumsCompact(
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = animatedContentScope,
      uiState = uiState,
      onAlbumTap = navigateToAlbumInfo,
      onScrollEnd = viewModel::fetchAlbums,
      onUpdateTimeRange = viewModel::updateTimeRange,
      topAppBarScrollBehavior = topAppBarScrollBehavior,
      modifier = modifier,
    )
  } else {
    val scaffoldState = rememberSunsetListDetailScaffoldState<AlbumKey>()
    SunsetListDetailScaffold(
      state = scaffoldState,
      modifier = modifier.fillMaxSize(),
      listPane = {
        val listPaneScope: AnimatedVisibilityScope = this
        TopAlbumsCompact(
          sharedTransitionScope = sharedTransitionScope,
          animatedVisibilityScope = listPaneScope,
          uiState = uiState,
          onAlbumTap = { album, id ->
            scaffoldState.selectDetail(
              AlbumKey(
                albumName = album.title,
                artistName = album.artist,
                artworkUrl = album.imageList.imageUrl() ?: "",
                contentId = id,
              )
            )
          },
          onScrollEnd = viewModel::fetchAlbums,
          onUpdateTimeRange = viewModel::updateTimeRange,
          topAppBarScrollBehavior = topAppBarScrollBehavior,
          useSharedElement = false,
          modifier = Modifier,
        )
      },
      detailPane = { selection: AlbumKey? ->
        val detailPaneScope: AnimatedVisibilityScope = this
        if (selection != null) {
          with(sharedTransitionScope) {
            AlbumPaneScreen(
              animatedVisibilityScope = detailPaneScope,
              id = "",
              viewModel = albumViewModelProvider(selection),
              onAlbumLoadMoreTap = { url ->
                if (url.isNotEmpty()) navigateToWebView(url)
              },
              onBackPressed = { scaffoldState.back() },
            )
          }
        }
      },
    )
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun TopAlbumsCompact(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  uiState: TopAlbumsUiState,
  onAlbumTap: (TopAlbumInfo, String) -> Unit,
  onScrollEnd: () -> Unit,
  onUpdateTimeRange: (TimeRangeFiltering) -> Unit,
  topAppBarScrollBehavior: SunsetTopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
  useSharedElement: Boolean = true,
) {
  var showBottomSheet by remember { mutableStateOf(false) }
  val bottomSheetState = rememberSunsetModalBottomSheetState()
  val coroutineScope = rememberCoroutineScope()
  val configuration = LocalConfiguration.current
  val orientation = remember { configuration.orientation }

  BackHandler(bottomSheetState.isVisible) {
    coroutineScope.launch { bottomSheetState.hide() }
  }

  SunsetScaffold(
    floatingActionButton = {
      FilteringFloatingButton(
        onClick = {
          coroutineScope.launch { showBottomSheet = true }
        },
        modifier = modifier.offset(y = (-64).dp),
      )
    },
  ) {
    TopAlbumsContent(
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = animatedVisibilityScope,
      albums = uiState.topAlbums,
      hasNext = uiState.hasNext,
      onScrollEnd = onScrollEnd,
      maxSpanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2,
      onAlbumTap = onAlbumTap,
      useSharedElement = useSharedElement,
      modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
    )

    if (showBottomSheet) {
      SunsetModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        sheetState = bottomSheetState,
      ) {
        FilteringBottomSheet(
          selectedTimeRangeFiltering = uiState.timeRangeFiltering,
          onClick = {
            coroutineScope.launch { bottomSheetState.hide() }
              .invokeOnCompletion { showBottomSheet = false }
            onUpdateTimeRange(it)
          },
        )
      }
    }
  }

  if (uiState.isLoading && uiState.topAlbums.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      LoadingIndicator(modifier = Modifier)
    }
  }
}

@Composable
private fun TopAlbumsContent(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  albums: ImmutableList<TopAlbumInfo>,
  hasNext: Boolean,
  maxSpanCount: Int,
  onAlbumTap: (TopAlbumInfo, String) -> Unit,
  onScrollEnd: () -> Unit,
  modifier: Modifier = Modifier,
  useSharedElement: Boolean = true,
) {
  LazyVerticalGrid(
    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    columns = GridCells.Fixed(maxSpanCount),
    content = {
      itemsIndexed(
        items = albums,
        key = { _, album -> "${album.hashCode()}" },
        contentType = { _, _ -> "top_albums" },
      ) { index, album ->
        val id = if (album.imageList.imageUrl().isInvalidArtwork()) {
          ""
        } else {
          "top_album_${index}${album.hashCode()}"
        }
        val sharedElementId = if (useSharedElement) id else ""
        TopAlbum(
          album = album,
          onAlbumTap = { onAlbumTap(album, id) },
          sharedTransitionScope = sharedTransitionScope,
          animatedVisibilityScope = animatedVisibilityScope,
          id = sharedElementId,
        )
      }

      if (hasNext && albums.isNotEmpty()) {
        item(
          key = "top_albums_loading",
          span = { GridItemSpan(maxLineSpan) },
        ) {
          InfiniteLoadingIndicator(
            onScrollEnd = onScrollEnd,
            modifier = Modifier,
          )
        }
      }
    },
    modifier = modifier.fillMaxSize(),
  )
}
