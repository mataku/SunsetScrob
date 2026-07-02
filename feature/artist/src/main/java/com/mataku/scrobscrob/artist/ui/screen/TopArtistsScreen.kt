package com.mataku.scrobscrob.artist.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.mataku.scrobscrob.artist.ui.molecule.TopArtist
import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel.TopArtistsUiState
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
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
fun TopArtistsScreen(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  viewModel: TopArtistsViewModel,
  onArtistTap: (TopArtistInfo, String) -> Unit,
  artistViewModelProvider: @Composable (ArtistKey) -> ArtistViewModel,
  navigateToWebView: (String) -> Unit,
  topAppBarScrollBehavior: SunsetTopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  if (isCompactWidth()) {
    TopArtistsCompact(
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = animatedContentScope,
      uiState = uiState,
      onArtistTap = onArtistTap,
      onScrollEnd = viewModel::fetchTopArtists,
      onUpdateTimeRange = viewModel::updateTimeRange,
      topAppBarScrollBehavior = topAppBarScrollBehavior,
      modifier = modifier,
    )
  } else {
    val scaffoldState = rememberSunsetListDetailScaffoldState<ArtistKey>()
    SunsetListDetailScaffold(
      state = scaffoldState,
      modifier = modifier.fillMaxSize(),
      listPane = {
        val listPaneScope: AnimatedVisibilityScope = this
        TopArtistsCompact(
          sharedTransitionScope = sharedTransitionScope,
          animatedVisibilityScope = listPaneScope,
          uiState = uiState,
          onArtistTap = { artist, id ->
            scaffoldState.selectDetail(
              ArtistKey(
                artistName = artist.name,
                artworkUrl = (artist.imageUrl ?: artist.imageList.imageUrl()) ?: "",
                contentId = id,
              )
            )
          },
          onScrollEnd = viewModel::fetchTopArtists,
          onUpdateTimeRange = viewModel::updateTimeRange,
          topAppBarScrollBehavior = topAppBarScrollBehavior,
          useSharedElement = false,
          modifier = Modifier,
        )
      },
      detailPane = { selection: ArtistKey? ->
        val detailPaneScope: AnimatedVisibilityScope = this
        if (selection != null) {
          with(sharedTransitionScope) {
            ArtistPaneScreen(
              animatedVisibilityScope = detailPaneScope,
              id = "",
              viewModel = artistViewModelProvider(selection),
              onArtistLoadMoreTap = { url ->
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
private fun TopArtistsCompact(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  uiState: TopArtistsUiState,
  onArtistTap: (TopArtistInfo, String) -> Unit,
  onScrollEnd: () -> Unit,
  onUpdateTimeRange: (TimeRangeFiltering) -> Unit,
  topAppBarScrollBehavior: SunsetTopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
  useSharedElement: Boolean = true,
) {
  val bottomSheetState = rememberSunsetModalBottomSheetState()
  var showBottomSheet by remember { mutableStateOf(false) }
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
    TopArtistsContent(
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = animatedVisibilityScope,
      artists = uiState.topArtists,
      hasNext = uiState.hasNext,
      onArtistTap = onArtistTap,
      onScrollEnd = onScrollEnd,
      maxSpanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2,
      useSharedElement = useSharedElement,
      modifier = Modifier
        .fillMaxSize()
        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
    )
    if (showBottomSheet) {
      SunsetModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        sheetState = bottomSheetState,
      ) {
        FilteringBottomSheet(
          selectedTimeRangeFiltering = uiState.selectedTimeRangeFiltering,
          onClick = {
            onUpdateTimeRange(it)
            coroutineScope.launch { bottomSheetState.hide() }
              .invokeOnCompletion { showBottomSheet = false }
          },
        )
      }
    }
  }

  if (uiState.isLoading && uiState.topArtists.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      LoadingIndicator(modifier = Modifier)
    }
  }
}

@Composable
private fun TopArtistsContent(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  artists: ImmutableList<TopArtistInfo>,
  hasNext: Boolean,
  maxSpanCount: Int,
  onArtistTap: (TopArtistInfo, String) -> Unit,
  onScrollEnd: () -> Unit,
  modifier: Modifier = Modifier,
  useSharedElement: Boolean = true,
) {
  LazyVerticalGrid(
    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    columns = GridCells.Fixed(maxSpanCount),
    content = {
      itemsIndexed(
        items = artists,
        key = { index, artist -> "${index}${artist.hashCode()}" },
      ) { index, artist ->
        val cachedImageUrl = artist.imageUrl
        val imageUrl = when {
          cachedImageUrl != null -> cachedImageUrl
          artist.imageList.isEmpty() -> null
          else -> artist.imageList.imageUrl()
        }
        val id = if (imageUrl.isInvalidArtwork()) {
          ""
        } else {
          "top_artist_${index}${artist.hashCode()}"
        }
        val sharedElementId = if (useSharedElement) id else ""
        TopArtist(
          artist = artist,
          onArtistTap = { onArtistTap.invoke(artist, id) },
          modifier = Modifier.fillMaxWidth(),
          sharedTransitionScope = sharedTransitionScope,
          animatedVisibilityScope = animatedVisibilityScope,
          id = sharedElementId,
          imageUrl = imageUrl ?: "",
        )
      }

      if (hasNext && artists.isNotEmpty()) {
        item(
          span = { GridItemSpan(maxLineSpan) },
          key = "top_artists_loading",
        ) {
          InfiniteLoadingIndicator(
            onScrollEnd = onScrollEnd,
            modifier = Modifier,
          )
        }
      }
    },
    modifier = modifier,
  )
}
